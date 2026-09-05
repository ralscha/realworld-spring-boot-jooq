package ch.rasc.realworld.controller;

import static ch.rasc.realworld.db.tables.AppUser.APP_USER;
import static ch.rasc.realworld.db.tables.Article.ARTICLE;
import static ch.rasc.realworld.db.tables.ArticleFavorite.ARTICLE_FAVORITE;
import static ch.rasc.realworld.db.tables.ArticleTag.ARTICLE_TAG;
import static ch.rasc.realworld.db.tables.Follow.FOLLOW;
import static ch.rasc.realworld.db.tables.Tag.TAG;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
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
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonRootName;

import ch.rasc.realworld.Util;
import ch.rasc.realworld.config.AuthenticatedUser;
import ch.rasc.realworld.dto.Article;
import ch.rasc.realworld.dto.ArticleDataList;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

@RestController
public class ArticlesController {

	private final DSLContext dsl;

	public ArticlesController(DSLContext dsl) {
		this.dsl = dsl;
	}

	@PostMapping("/articles")
	@Transactional
	public ResponseEntity<?> createArticle(@Valid @RequestBody NewArticleParam newArticleParam,
			BindingResult bindingResult, @AuthenticationPrincipal AuthenticatedUser user) {

		if (bindingResult.hasErrors()) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Util.toError(bindingResult));
		}

		var articleRecord = this.dsl.newRecord(ARTICLE);
		articleRecord.setTitle(newArticleParam.title);
		articleRecord.setDescription(newArticleParam.description);
		articleRecord.setBody(newArticleParam.body);
		LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
		articleRecord.setCreatedAt(now);
		articleRecord.setUpdatedAt(now);
		articleRecord.setSlug(Util.uniqueSlug(this.dsl, newArticleParam.title, null));
		articleRecord.setUserId(user.getId());
		articleRecord.store();

		if (newArticleParam.tagList != null) {
			Util.setArticleTags(this.dsl, articleRecord.getId(), newArticleParam.tagList);
		}

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(Map.of("article", Util.getArticle(this.dsl, articleRecord.getId(), user.getId())));
	}

	@GetMapping(path = "/articles/feed")
	public ResponseEntity<ArticleDataList> getFeed(
			@RequestParam(value = "offset", defaultValue = "0") @Min(0) int offset,
			@RequestParam(value = "limit", defaultValue = "20") @Min(1) int limit,
			@AuthenticationPrincipal AuthenticatedUser user) {

		var followUserIds = this.dsl.select(FOLLOW.FOLLOW_ID)
			.from(FOLLOW)
			.where(FOLLOW.USER_ID.eq(user.getId()))
			.fetchSet(FOLLOW.FOLLOW_ID);
		if (followUserIds.isEmpty()) {
			return ResponseEntity.ok(new ArticleDataList(List.of(), 0));
		}

		var articlesRecords = this.dsl.selectFrom(ARTICLE)
			.where(ARTICLE.USER_ID.in(followUserIds))
			.orderBy(ARTICLE.CREATED_AT.desc(), ARTICLE.ID.desc())
			.offset(offset)
			.limit(limit)
			.fetch();

		int articlesCount = this.dsl.selectCount()
			.from(ARTICLE)
			.where(ARTICLE.USER_ID.in(followUserIds))
			.fetchOne(0, int.class);

		List<Article> articles = new ArrayList<>();
		for (var articleRecord : articlesRecords) {
			articles.add(Util.getArticleSummary(this.dsl, articleRecord, user.getId()));
		}
		return ResponseEntity.ok(new ArticleDataList(articles, articlesCount));

	}

	@GetMapping("/articles")
	public ResponseEntity<ArticleDataList> getArticles(
			@RequestParam(value = "offset", defaultValue = "0") @Min(0) int offset,
			@RequestParam(value = "limit", defaultValue = "20") @Min(1) int limit,
			@RequestParam(value = "tag", required = false) String tag,
			@RequestParam(value = "favorited", required = false) String favoritedBy,
			@RequestParam(value = "author", required = false) String author,
			@AuthenticationPrincipal AuthenticatedUser user) {

		List<Condition> filters = new ArrayList<>();

		if (StringUtils.hasText(tag)) {
			filters.add(DSL.exists(this.dsl.selectOne()
				.from(ARTICLE_TAG)
				.innerJoin(TAG)
				.onKey()
				.where(ARTICLE_TAG.ARTICLE_ID.eq(ARTICLE.ID).and(TAG.NAME.eq(tag)))));
		}

		if (StringUtils.hasText(favoritedBy)) {
			filters.add(DSL.exists(this.dsl.selectOne()
				.from(ARTICLE_FAVORITE)
				.innerJoin(APP_USER)
				.on(ARTICLE_FAVORITE.USER_ID.eq(APP_USER.ID))
				.where(ARTICLE_FAVORITE.ARTICLE_ID.eq(ARTICLE.ID).and(APP_USER.USERNAME.eq(favoritedBy)))));
		}

		if (StringUtils.hasText(author)) {
			filters.add(DSL.exists(this.dsl.selectOne()
				.from(APP_USER)
				.where(APP_USER.ID.eq(ARTICLE.USER_ID).and(APP_USER.USERNAME.eq(author)))));
		}

		Condition condition = DSL.and(filters);
		var articlesRecords = this.dsl.selectFrom(ARTICLE)
			.where(condition)
			.orderBy(ARTICLE.CREATED_AT.desc(), ARTICLE.ID.desc())
			.offset(offset)
			.limit(limit)
			.fetch();
		int articlesCount = this.dsl.fetchCount(ARTICLE, condition);

		if (articlesCount == 0) {
			return ResponseEntity.ok(new ArticleDataList(List.of(), articlesCount));
		}

		List<Article> articles = new ArrayList<>();
		for (var articleRecord : articlesRecords) {
			articles.add(Util.getArticleSummary(this.dsl, articleRecord, user != null ? user.getId() : -1));
		}
		return ResponseEntity.ok(new ArticleDataList(articles, articlesCount));
	}

}

@JsonRootName("article")
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
class NewArticleParam {

	@NotBlank(message = "can't be empty")
	@Size(max = 255, message = "is too long")
	String title;

	@NotBlank(message = "can't be empty")
	String description;

	@NotBlank(message = "can't be empty")
	String body;

	List<@NotBlank(message = "can't be empty") @Size(max = 255, message = "is too long") String> tagList;

}
