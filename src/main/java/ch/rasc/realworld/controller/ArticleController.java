package ch.rasc.realworld.controller;

import static ch.rasc.realworld.db.tables.Article.ARTICLE;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.jooq.DSLContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

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
	@Transactional
	public ResponseEntity<?> updateArticle(@PathVariable("slug") String slug,
			@AuthenticationPrincipal AuthenticatedUser user, @Valid @RequestBody UpdateArticleParam updateArticleParam,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Util.toError(bindingResult));
		}

		var articleRecord = this.dsl.selectFrom(ARTICLE).where(ARTICLE.SLUG.eq(slug)).fetchOne();
		if (articleRecord == null) {
			return ResponseEntity.notFound().build();
		}
		if (articleRecord.getUserId() != user.getId()) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		if (updateArticleParam.title != null) {
			articleRecord.setTitle(updateArticleParam.title);
			articleRecord.setSlug(Util.uniqueSlug(this.dsl, updateArticleParam.title, articleRecord.getId()));
		}
		if (updateArticleParam.body != null) {
			articleRecord.setBody(updateArticleParam.body);
		}
		if (updateArticleParam.description != null) {
			articleRecord.setDescription(updateArticleParam.description);
		}
		articleRecord.setUpdatedAt(LocalDateTime.now(Clock.systemUTC()));
		articleRecord.update();
		if (updateArticleParam.tagList != null) {
			Util.setArticleTags(this.dsl, articleRecord.getId(), updateArticleParam.tagList);
		}
		Article article = Util.getArticle(this.dsl, articleRecord, user.getId());
		return ResponseEntity.ok().body(Map.of("article", article));
	}

	@DeleteMapping("/articles/{slug}")
	@Transactional
	public ResponseEntity<?> deleteArticle(@PathVariable("slug") String slug,
			@AuthenticationPrincipal AuthenticatedUser user) {

		var articleRecord = this.dsl.select(ARTICLE.ID, ARTICLE.USER_ID)
			.from(ARTICLE)
			.where(ARTICLE.SLUG.eq(slug))
			.fetchOne();
		if (articleRecord != null) {
			if (articleRecord.get(ARTICLE.USER_ID) == user.getId()) {
				this.dsl.delete(ARTICLE).where(ARTICLE.ID.eq(articleRecord.get(ARTICLE.ID))).execute();
				Util.removeUnusedTags(this.dsl);
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

	@Pattern(regexp = "(?s).*\\S.*", message = "can't be empty")
	@Size(max = 255, message = "is too long")
	String title;

	@Pattern(regexp = "(?s).*\\S.*", message = "can't be empty")
	String body;

	@Pattern(regexp = "(?s).*\\S.*", message = "can't be empty")
	String description;

	List<@NotBlank(message = "can't be empty") @Size(max = 255, message = "is too long") String> tagList;

}
