package ch.rasc.realworld.dto;

import java.time.LocalDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Article {
	private final String slug;

	private final String title;

	private final String description;

	private final String body;

	private final Set<String> tagList;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private final LocalDateTime createdAt;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyy-MM-dd'T'HH:mm:ss.SSS'Z'")
	private final LocalDateTime updatedAt;

	private final boolean favorited;

	private final int favoritesCount;

	private final Profile author;

	public Article(String slug, String title, String description, String body,
			Set<String> tagList, LocalDateTime createdAt, LocalDateTime updatedAt,
			boolean favorited, int favoritesCount, Profile author) {
		this.slug = slug;
		this.title = title;
		this.description = description;
		this.body = body;
		this.tagList = tagList;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.favorited = favorited;
		this.favoritesCount = favoritesCount;
		this.author = author;
	}

	public String getSlug() {
		return this.slug;
	}

	public String getTitle() {
		return this.title;
	}

	public String getDescription() {
		return this.description;
	}

	public String getBody() {
		return this.body;
	}

	public Set<String> getTagList() {
		return this.tagList;
	}

	public LocalDateTime getCreatedAt() {
		return this.createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return this.updatedAt;
	}

	public boolean isFavorited() {
		return this.favorited;
	}

	public int getFavoritesCount() {
		return this.favoritesCount;
	}

	public Profile getAuthor() {
		return this.author;
	}

}
