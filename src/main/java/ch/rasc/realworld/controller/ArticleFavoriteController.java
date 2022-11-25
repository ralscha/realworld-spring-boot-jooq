package ch.rasc.realworld.controller;

import static ch.rasc.realworld.db.tables.Article.ARTICLE;
import static ch.rasc.realworld.db.tables.ArticleFavorite.ARTICLE_FAVORITE;

import java.util.Map;

import org.jooq.DSLContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.rasc.realworld.Util;
import ch.rasc.realworld.config.AuthenticatedUser;

@RestController
public class ArticleFavoriteController {
	private final DSLContext dsl;

	public ArticleFavoriteController(DSLContext dsl) {
		this.dsl = dsl;
	}

	@PostMapping("articles/{slug}/favorite")
	public ResponseEntity<Map<String, Object>> favoriteArticle(@PathVariable("slug") String slug,
			@AuthenticationPrincipal AuthenticatedUser user) {
		var record = this.dsl.select(ARTICLE.ID).from(ARTICLE).where(ARTICLE.SLUG.eq(slug)).fetchOne();
		if (record != null) {
			var newRecord = this.dsl.newRecord(ARTICLE_FAVORITE);
			newRecord.setArticleId(record.get(ARTICLE.ID));
			newRecord.setUserId(user.getId());
			newRecord.store();
		}

		return ResponseEntity.ok().body(Map.of("article", Util.getArticle(this.dsl, slug, user.getId())));
	}

	@DeleteMapping("articles/{slug}/favorite")
	public ResponseEntity<Map<String, Object>> unfavoriteArticle(@PathVariable("slug") String slug,
			@AuthenticationPrincipal AuthenticatedUser user) {

		var record = this.dsl.select(ARTICLE.ID).from(ARTICLE).where(ARTICLE.SLUG.eq(slug)).fetchOne();
		if (record != null) {
			this.dsl.delete(ARTICLE_FAVORITE).where(ARTICLE_FAVORITE.USER_ID.eq(user.getId())
					.and(ARTICLE_FAVORITE.ARTICLE_ID.eq(record.get(ARTICLE.ID)))).execute();
		}

		return ResponseEntity.ok().body(Map.of("article", Util.getArticle(this.dsl, slug, user.getId())));
	}

}
