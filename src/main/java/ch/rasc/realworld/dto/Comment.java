package ch.rasc.realworld.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import ch.rasc.realworld.db.tables.records.CommentRecord;

public class Comment {
	private final long id;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private final LocalDateTime createdAt;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private final LocalDateTime updatedAt;

	private final String body;

	private final Profile author;

	public Comment(CommentRecord newRecord, Profile author) {
		this.id = newRecord.getId();
		this.createdAt = newRecord.getCreatedAt();
		this.updatedAt = newRecord.getUpdatedAt();
		this.body = newRecord.getBody();
		this.author = author;
	}

	public long getId() {
		return this.id;
	}

	public LocalDateTime getCreatedAt() {
		return this.createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return this.updatedAt;
	}

	public String getBody() {
		return this.body;
	}

	public Profile getAuthor() {
		return this.author;
	}

}
