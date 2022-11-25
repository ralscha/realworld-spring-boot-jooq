package ch.rasc.realworld.controller;

import static ch.rasc.realworld.db.tables.AppUser.APP_USER;
import static ch.rasc.realworld.db.tables.Article.ARTICLE;
import static ch.rasc.realworld.db.tables.Comment.COMMENT;
import static ch.rasc.realworld.db.tables.Follow.FOLLOW;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jooq.DSLContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonRootName;

import ch.rasc.realworld.Util;
import ch.rasc.realworld.config.AuthenticatedUser;
import ch.rasc.realworld.db.tables.records.CommentRecord;
import ch.rasc.realworld.dto.Comment;
import ch.rasc.realworld.dto.Profile;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
public class CommentsController {

	private final DSLContext dsl;

	public CommentsController(DSLContext dsl) {
		this.dsl = dsl;
	}

	@PostMapping("/articles/{slug}/comments")
	public ResponseEntity<?> createComment(@PathVariable("slug") String slug,
			@AuthenticationPrincipal AuthenticatedUser user, @Valid @RequestBody NewCommentParam newCommentParam,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Util.toError(bindingResult));
		}

		var record = this.dsl.select(ARTICLE.ID).from(ARTICLE).where(ARTICLE.SLUG.eq(slug)).fetchOne();
		var newComment = this.dsl.newRecord(COMMENT);
		newComment.setArticleId(record.get(ARTICLE.ID));
		newComment.setBody(newCommentParam.body);
		newComment.setCreatedAt(OffsetDateTime.now());
		newComment.setUpdatedAt(OffsetDateTime.now());
		newComment.setUserId(user.getId());
		newComment.store();

		Profile author = new Profile(user.getUsername(), user.getBio(), user.getImage(), false);
		return ResponseEntity.status(201).body(Map.of("comment", new Comment(newComment.getId(),
				newComment.getCreatedAt(), newComment.getUpdatedAt(), newComment.getBody(), author)));
	}

	@GetMapping("/articles/{slug}/comments")
	public ResponseEntity<?> getComments(@PathVariable("slug") String slug,
			@AuthenticationPrincipal AuthenticatedUser user) {

		var record = this.dsl.select(ARTICLE.ID).from(ARTICLE).where(ARTICLE.SLUG.eq(slug)).fetchOne();

		List<Comment> comments = new ArrayList<>();
		var commentRecords = this.dsl.selectFrom(COMMENT).where(COMMENT.ARTICLE_ID.eq(record.get(ARTICLE.ID))).fetch();

		Set<Long> followUserIds = new HashSet<>();
		if (user != null) {
			followUserIds = this.dsl.select(FOLLOW.FOLLOW_ID).from(FOLLOW).where(FOLLOW.USER_ID.eq(user.getId()))
					.fetchSet(FOLLOW.FOLLOW_ID);
		}
		for (CommentRecord commentRecord : commentRecords) {
			var userRecord = this.dsl.selectFrom(APP_USER).where(APP_USER.ID.eq(commentRecord.getUserId())).fetchOne();
			Profile author = new Profile(userRecord.getUsername(), userRecord.getBio(), userRecord.getImage(),
					followUserIds.contains(userRecord.getId()));
			comments.add(new Comment(commentRecord.getId(), commentRecord.getCreatedAt(), commentRecord.getUpdatedAt(),
					commentRecord.getBody(), author));
		}
		return ResponseEntity.ok(Map.of("comments", comments));
	}

	@DeleteMapping(path = "/articles/{slug}/comments/{id}")
	public ResponseEntity<?> deleteComment(@PathVariable("slug") String slug, @PathVariable("id") long commentId,
			@AuthenticationPrincipal AuthenticatedUser user) {

		var articleRecord = this.dsl.select(ARTICLE.ID, ARTICLE.USER_ID).from(ARTICLE).where(ARTICLE.SLUG.eq(slug))
				.fetchOne();

		if (articleRecord == null) {
			return ResponseEntity.notFound().build();
		}

		var commentRecord = this.dsl.select(COMMENT.ID, COMMENT.USER_ID).from(COMMENT)
				.where(COMMENT.ID.eq(commentId).and(COMMENT.ARTICLE_ID.eq(articleRecord.get(ARTICLE.ID)))).fetchOne();

		if (commentRecord == null) {
			return ResponseEntity.notFound().build();
		}

		if (((user.getId() != articleRecord.get(ARTICLE.USER_ID))
				&& (user.getId() != commentRecord.get(COMMENT.USER_ID)))) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}

		this.dsl.delete(COMMENT).where(COMMENT.ID.eq(commentRecord.get(COMMENT.ID))).execute();

		return ResponseEntity.noContent().build();

	}

}

@JsonRootName("comment")
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
class NewCommentParam {
	@NotBlank(message = "can't be empty")
	String body;
}
