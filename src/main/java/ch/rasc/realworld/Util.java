package ch.rasc.realworld;

import static ch.rasc.realworld.db.tables.AppUser.APP_USER;
import static ch.rasc.realworld.db.tables.Article.ARTICLE;
import static ch.rasc.realworld.db.tables.ArticleFavorite.ARTICLE_FAVORITE;
import static ch.rasc.realworld.db.tables.ArticleTag.ARTICLE_TAG;
import static ch.rasc.realworld.db.tables.Follow.FOLLOW;
import static ch.rasc.realworld.db.tables.Tag.TAG;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jooq.DSLContext;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import ch.rasc.realworld.db.tables.records.AppUserRecord;
import ch.rasc.realworld.db.tables.records.ArticleRecord;
import ch.rasc.realworld.dto.Article;
import ch.rasc.realworld.dto.Profile;

public final class Util {

	private static final int MAX_SLUG_LENGTH = 255;

	private Util() {
	}

	public static String toSlug(String title) {
		String slug = Normalizer.normalize(title, Normalizer.Form.NFKD)
			.replaceAll("\\p{M}+", "")
			.toLowerCase(Locale.ROOT)
			.replaceAll("[^\\p{L}\\p{N}]+", "-")
			.replaceAll("^-+|-+$", "");
		return slug.isEmpty() ? "article" : slug;
	}

	public static String uniqueSlug(DSLContext dsl, String title, Long excludedArticleId) {
		String base = truncate(toSlug(title), MAX_SLUG_LENGTH);
		String candidate = base;
		int suffix = 2;
		while (slugExists(dsl, candidate, excludedArticleId)) {
			String ending = "-" + suffix++;
			candidate = truncate(base, MAX_SLUG_LENGTH - ending.length()) + ending;
		}
		return candidate;
	}

	private static boolean slugExists(DSLContext dsl, String slug, Long excludedArticleId) {
		var condition = ARTICLE.SLUG.eq(slug);
		if (excludedArticleId != null) {
			condition = condition.and(ARTICLE.ID.ne(excludedArticleId));
		}
		return dsl.fetchExists(ARTICLE, condition);
	}

	private static String truncate(String value, int length) {
		return value.length() <= length ? value : value.substring(0, length);
	}

	public static Map<String, Map<String, List<String>>> toError(BindingResult bindingResult) {
		Map<String, List<String>> errors = new LinkedHashMap<>();
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			errors.computeIfAbsent(fieldError.getField(), key -> new ArrayList<>()).add(fieldError.getDefaultMessage());
		}
		return Map.of("errors", errors);
	}

	public static void setArticleTags(DSLContext dsl, long articleId, List<String> requestedTags) {
		dsl.deleteFrom(ARTICLE_TAG).where(ARTICLE_TAG.ARTICLE_ID.eq(articleId)).execute();
		var tags = new LinkedHashSet<String>();
		for (String tag : requestedTags) {
			tags.add(tag.trim());
		}
		for (String name : tags) {
			Long tagId = dsl.select(TAG.ID).from(TAG).where(TAG.NAME.eq(name)).fetchOne(TAG.ID);
			if (tagId == null) {
				var tagRecord = dsl.newRecord(TAG);
				tagRecord.setName(name);
				tagRecord.store();
				tagId = tagRecord.getId();
			}
			dsl.insertInto(ARTICLE_TAG, ARTICLE_TAG.ARTICLE_ID, ARTICLE_TAG.TAG_ID).values(articleId, tagId).execute();
		}
		removeUnusedTags(dsl);
	}

	public static void removeUnusedTags(DSLContext dsl) {
		dsl.deleteFrom(TAG)
			.whereNotExists(dsl.selectOne().from(ARTICLE_TAG).where(ARTICLE_TAG.TAG_ID.eq(TAG.ID)))
			.execute();
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
		return getArticle(dsl, record, userId, true);
	}

	public static Article getArticleSummary(DSLContext dsl, ArticleRecord record, long userId) {
		return getArticle(dsl, record, userId, false);
	}

	private static Article getArticle(DSLContext dsl, ArticleRecord record, long userId, boolean includeBody) {
		if (record == null) {
			return null;
		}

		long articleUserId = record.get(ARTICLE.USER_ID);
		long articleId = record.getId();
		AppUserRecord authorRecord = dsl.selectFrom(APP_USER).where(APP_USER.ID.eq(articleUserId)).fetchOne();

		Profile author = null;
		if (authorRecord != null) {
			boolean following = dsl.fetchExists(FOLLOW,
					FOLLOW.USER_ID.eq(userId).and(FOLLOW.FOLLOW_ID.eq(authorRecord.getId())));
			author = new Profile(authorRecord.get(APP_USER.USERNAME), authorRecord.get(APP_USER.BIO),
					authorRecord.get(APP_USER.IMAGE), following);
		}

		boolean favorited = dsl.fetchExists(ARTICLE_FAVORITE,
				ARTICLE_FAVORITE.USER_ID.eq(userId).and(ARTICLE_FAVORITE.ARTICLE_ID.eq(articleId)));
		int favoritesCount = dsl.fetchCount(ARTICLE_FAVORITE, ARTICLE_FAVORITE.ARTICLE_ID.eq(articleId));

		return new Article(record.get(ARTICLE.SLUG), record.get(ARTICLE.TITLE), record.get(ARTICLE.DESCRIPTION),
				includeBody ? record.get(ARTICLE.BODY) : null,
				dsl.select(TAG.NAME)
					.from(TAG)
					.innerJoin(ARTICLE_TAG)
					.onKey()
					.where(ARTICLE_TAG.ARTICLE_ID.eq(articleId))
					.orderBy(TAG.NAME)
					.fetch(TAG.NAME),
				record.get(ARTICLE.CREATED_AT), record.get(ARTICLE.UPDATED_AT), favorited, favoritesCount, author);
	}

}
