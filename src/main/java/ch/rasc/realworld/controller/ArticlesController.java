package ch.rasc.realworld.controller;

import static ch.rasc.realworld.db.tables.AppUser.APP_USER;
import static ch.rasc.realworld.db.tables.Article.ARTICLE;
import static ch.rasc.realworld.db.tables.ArticleFavorite.ARTICLE_FAVORITE;
import static ch.rasc.realworld.db.tables.ArticleTag.ARTICLE_TAG;
import static ch.rasc.realworld.db.tables.Follow.FOLLOW;
import static ch.rasc.realworld.db.tables.Tag.TAG;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jooq.DSLContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonRootName;

import ch.rasc.realworld.Util;
import ch.rasc.realworld.config.AuthenticatedUser;
import ch.rasc.realworld.db.tables.records.ArticleRecord;
import ch.rasc.realworld.dto.Article;
import ch.rasc.realworld.dto.ArticleDataList;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
public class ArticlesController {
	private final DSLContext dsl;

	public ArticlesController(DSLContext dsl) {
		this.dsl = dsl;
	}

	@PostMapping("/articles")
	public ResponseEntity<?> createArticle(@Valid @RequestBody NewArticleParam newArticleParam,
			BindingResult bindingResult, @AuthenticationPrincipal AuthenticatedUser user) {

		if (bindingResult.hasErrors()) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Util.toError(bindingResult));
		}

		var articleRecord = this.dsl.newRecord(ARTICLE);
		articleRecord.setTitle(newArticleParam.title);
		articleRecord.setDescription(newArticleParam.description);
		articleRecord.setBody(newArticleParam.body);
		articleRecord.setCreatedAt(OffsetDateTime.now());
		articleRecord.setUpdatedAt(OffsetDateTime.now());
		articleRecord.setSlug(Util.toSlug(newArticleParam.title));
		articleRecord.setUserId(user.getId());
		articleRecord.store();

		if (newArticleParam.tagList != null) {
			for (String tag : newArticleParam.tagList) {
				var tagRecord = this.dsl.select(TAG.ID).from(TAG).where(TAG.NAME.eq(tag)).fetchOne();
				if (tagRecord == null) {
					var newTagRecord = this.dsl.newRecord(TAG);
					newTagRecord.setName(tag);
					newTagRecord.store();
					this.dsl.insertInto(ARTICLE_TAG, ARTICLE_TAG.ARTICLE_ID, ARTICLE_TAG.TAG_ID)
							.values(articleRecord.getId(), newTagRecord.getId()).execute();
				} else {
					this.dsl.insertInto(ARTICLE_TAG, ARTICLE_TAG.ARTICLE_ID, ARTICLE_TAG.TAG_ID)
							.values(articleRecord.getId(), tagRecord.get(TAG.ID)).execute();
				}
			}
		}

		return ResponseEntity.ok(Map.of("article", Util.getArticle(this.dsl, articleRecord.getId(), user.getId())));
	}

	@GetMapping(path = "/articles/feed")
	public ResponseEntity<ArticleDataList> getFeed(@RequestParam(value = "offset", defaultValue = "0") int offset,
			@RequestParam(value = "limit", defaultValue = "20") int limit,
			@AuthenticationPrincipal AuthenticatedUser user) {

		var followUserIds = this.dsl.select(FOLLOW.FOLLOW_ID).from(FOLLOW).where(FOLLOW.USER_ID.eq(user.getId()))
				.fetch();
		if (followUserIds.isEmpty()) {
			return ResponseEntity.ok(new ArticleDataList(List.of(), 0));
		}

		var articlesRecords = this.dsl.selectFrom(ARTICLE).where(ARTICLE.USER_ID.in(followUserIds)).offset(offset)
				.limit(limit).fetch();

		int articlesCount = this.dsl.selectCount().from(ARTICLE).where(ARTICLE.USER_ID.in(followUserIds)).fetchOne(0,
				int.class);

		List<Article> articles = new ArrayList<>();
		for (var articleRecord : articlesRecords) {
			articles.add(Util.getArticle(this.dsl, articleRecord, user.getId()));
		}
		return ResponseEntity.ok(new ArticleDataList(articles, articlesCount));

	}

	@GetMapping("/articles")
	public ResponseEntity<ArticleDataList> getArticles(@RequestParam(value = "offset", defaultValue = "0") int offset,
			@RequestParam(value = "limit", defaultValue = "20") int limit,
			@RequestParam(value = "tag", required = false) String tag,
			@RequestParam(value = "favorited", required = false) String favoritedBy,
			@RequestParam(value = "author", required = false) String author,
			@AuthenticationPrincipal AuthenticatedUser user) {

		var articlesQuery = this.dsl.select(ARTICLE.fields()).from(ARTICLE);
		var articlesCountQuery = this.dsl.selectCount().from(ARTICLE);

		if (StringUtils.hasText(tag)) {
			articlesQuery.innerJoin(ARTICLE_TAG).on(ARTICLE_TAG.ARTICLE_ID.eq(ARTICLE.ID)).innerJoin(TAG).onKey()
					.where(TAG.NAME.eq(tag));
			articlesCountQuery.innerJoin(ARTICLE_TAG).on(ARTICLE_TAG.ARTICLE_ID.eq(ARTICLE.ID)).innerJoin(TAG).onKey()
					.where(TAG.NAME.eq(tag));
		}

		if (StringUtils.hasText(favoritedBy)) {
			articlesQuery.innerJoin(ARTICLE_FAVORITE).on(ARTICLE_FAVORITE.ARTICLE_ID.eq(ARTICLE.ID)).innerJoin(APP_USER)
					.on(ARTICLE_FAVORITE.USER_ID.eq(APP_USER.ID)).where(APP_USER.USERNAME.eq(favoritedBy));
			articlesCountQuery.innerJoin(ARTICLE_FAVORITE).on(ARTICLE_FAVORITE.ARTICLE_ID.eq(ARTICLE.ID))
					.innerJoin(APP_USER).on(ARTICLE_FAVORITE.USER_ID.eq(APP_USER.ID))
					.where(APP_USER.USERNAME.eq(favoritedBy));
		}

		if (StringUtils.hasText(author)) {
			articlesQuery.innerJoin(APP_USER).onKey().where(APP_USER.USERNAME.eq(author));
			articlesCountQuery.innerJoin(APP_USER).onKey().where(APP_USER.USERNAME.eq(author));
		}

		var articlesRecords = articlesQuery.offset(offset).limit(limit).fetch();
		int articlesCount = articlesCountQuery.fetchOne(0, int.class);

		if (articlesCount == 0) {
			return ResponseEntity.ok(new ArticleDataList(List.of(), articlesCount));
		}

		List<Article> articles = new ArrayList<>();
		for (var articleRecord : articlesRecords) {
			ArticleRecord record = new ArticleRecord(articleRecord.get(ARTICLE.ID), articleRecord.get(ARTICLE.USER_ID),
					articleRecord.get(ARTICLE.SLUG), articleRecord.get(ARTICLE.TITLE),
					articleRecord.get(ARTICLE.DESCRIPTION), articleRecord.get(ARTICLE.BODY),
					articleRecord.get(ARTICLE.CREATED_AT), articleRecord.get(ARTICLE.UPDATED_AT));
			articles.add(Util.getArticle(this.dsl, record, user != null ? user.getId() : -1));
		}
		return ResponseEntity.ok(new ArticleDataList(articles, articlesCount));
	}
}

@JsonRootName("article")
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
class NewArticleParam {
	@NotBlank(message = "can't be empty")
	String title;
	@NotBlank(message = "can't be empty")
	String description;
	@NotBlank(message = "can't be empty")
	String body;
	String[] tagList;

}