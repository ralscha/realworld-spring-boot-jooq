package ch.rasc.realworld;

import static ch.rasc.realworld.db.tables.AppUser.APP_USER;
import static ch.rasc.realworld.db.tables.Article.ARTICLE;
import static ch.rasc.realworld.db.tables.ArticleFavorite.ARTICLE_FAVORITE;
import static ch.rasc.realworld.db.tables.ArticleTag.ARTICLE_TAG;
import static ch.rasc.realworld.db.tables.Follow.FOLLOW;
import static ch.rasc.realworld.db.tables.Tag.TAG;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jooq.DSLContext;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import ch.rasc.realworld.db.tables.records.AppUserRecord;
import ch.rasc.realworld.db.tables.records.ArticleRecord;
import ch.rasc.realworld.dto.Article;
import ch.rasc.realworld.dto.Profile;

public class Util {

	public static String toSlug(String title) {
		return title.toLowerCase().replaceAll("[\\&|[\\uFE30-\\uFFA0]|\\’|\\”|\\s\\?\\,\\.]+", "-");
	}

	public static Map<String, Set<String>> toError(BindingResult bindingResult) {
		Map<String, Set<String>> errors = new HashMap<>();
		for (FieldError fe : bindingResult.getFieldErrors()) {
			errors.computeIfAbsent(fe.getField(), k -> new HashSet<>()).add(fe.getDefaultMessage());
		}
		return errors;
	}

	public static Article getArticle(DSLContext dsl, long articleId, long userId) {
		ArticleRecord record = dsl.selectFrom(ARTICLE).where(ARTICLE.ID.eq(articleId)).fetchOne();
		return getArticle(dsl, record, userId);
	}

	public static Article getArticle(DSLContext dsl, String slug, long userId) {
		ArticleRecord record = dsl.selectFrom(ARTICLE).where(ARTICLE.SLUG.eq(slug)).fetchOne();
		return getArticle(dsl, record, userId);
	}

	public static Article getArticle(DSLContext dsl, ArticleRecord record, long userId) {

		if (record != null) {
			long articleUserId = record.get(ARTICLE.USER_ID);
			long articleId = record.getId();

			AppUserRecord authorRecord = dsl.selectFrom(APP_USER).where(APP_USER.ID.eq(articleUserId)).fetchOne();

			Profile author = null;
			if (authorRecord != null) {
				boolean following = dsl.selectCount().from(FOLLOW)
						.where(FOLLOW.USER_ID.eq(userId).and(FOLLOW.FOLLOW_ID.eq(authorRecord.getId())))
						.fetchOne(0, int.class) == 1;

				author = new Profile(authorRecord.get(APP_USER.USERNAME), authorRecord.get(APP_USER.BIO),
						authorRecord.get(APP_USER.IMAGE), following);
			}

			boolean favorited = dsl.selectCount().from(ARTICLE_FAVORITE)
					.where(ARTICLE_FAVORITE.USER_ID.eq(userId).and(ARTICLE_FAVORITE.ARTICLE_ID.eq(articleId)))
					.fetchOne(0, int.class) == 1;
			int favoritesCount = dsl.selectCount().from(ARTICLE_FAVORITE)
					.where(ARTICLE_FAVORITE.ARTICLE_ID.eq(articleId)).fetchOne(0, int.class);

			return new Article(record.get(ARTICLE.SLUG), record.get(ARTICLE.TITLE), record.get(ARTICLE.DESCRIPTION),
					record.get(ARTICLE.BODY),
					dsl.select(TAG.NAME).from(TAG).innerJoin(ARTICLE_TAG).onKey()
							.where(ARTICLE_TAG.ARTICLE_ID.eq(articleId)).orderBy(TAG.NAME).fetchSet(TAG.NAME),
					record.get(ARTICLE.CREATED_AT), record.get(ARTICLE.UPDATED_AT), favorited, favoritesCount, author);

		}
		return null;
	}
}
