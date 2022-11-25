package ch.rasc.realworld.controller;

import static ch.rasc.realworld.db.tables.Article.ARTICLE;

import java.util.Map;

import org.jooq.DSLContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonRootName;

import ch.rasc.realworld.Util;
import ch.rasc.realworld.config.AuthenticatedUser;
import ch.rasc.realworld.dto.Article;
import jakarta.validation.Valid;

@RestController
public class ArticleController {

	private final DSLContext dsl;

	public ArticleController(DSLContext dsl) {
		this.dsl = dsl;
	}

	@GetMapping("/articles/{slug}")
	public ResponseEntity<Map<String, Article>> getArticlesWithSlug(@PathVariable("slug") String slug,
			@AuthenticationPrincipal AuthenticatedUser user) {

		Article article = Util.getArticle(this.dsl, slug, user != null ? user.getId() : -1);
		if (article != null) {
			return ResponseEntity.ok().body(Map.of("article", article));
		}

		return ResponseEntity.notFound().build();
	}

	@PutMapping("/articles/{slug}")
	public ResponseEntity<?> updateArticle(@PathVariable("slug") String slug,
			@AuthenticationPrincipal AuthenticatedUser user,
			@Valid @RequestBody UpdateArticleParam updateArticleParam) {
		var articleRecord = this.dsl.select(ARTICLE.ID, ARTICLE.USER_ID).from(ARTICLE).where(ARTICLE.SLUG.eq(slug))
				.fetchOne();
		if (articleRecord != null) {
			if (articleRecord.get(ARTICLE.USER_ID) == user.getId()) {
				this.dsl.update(ARTICLE).set(ARTICLE.TITLE, updateArticleParam.title)
						.set(ARTICLE.BODY, updateArticleParam.body)
						.set(ARTICLE.DESCRIPTION, updateArticleParam.description).execute();
				Article article = Util.getArticle(this.dsl, slug, user.getId());
				if (article != null) {
					return ResponseEntity.ok().body(Map.of("article", article));
				}
			}
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/articles/{slug}")
	public ResponseEntity<?> deleteArticle(@PathVariable("slug") String slug,
			@AuthenticationPrincipal AuthenticatedUser user) {

		var articleRecord = this.dsl.select(ARTICLE.ID, ARTICLE.USER_ID).from(ARTICLE).where(ARTICLE.SLUG.eq(slug))
				.fetchOne();
		if (articleRecord != null) {
			if (articleRecord.get(ARTICLE.USER_ID) == user.getId()) {
				this.dsl.delete(ARTICLE).where(ARTICLE.ID.eq(articleRecord.get(ARTICLE.ID))).execute();
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return ResponseEntity.notFound().build();
	}

}

@JsonRootName("article")
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
class UpdateArticleParam {
	String title = "";
	String body = "";
	String description = "";
}
