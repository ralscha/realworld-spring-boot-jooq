package ch.rasc.realworld.dto;

import java.time.OffsetDateTime;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

public record Article(String slug, String title, String description, String body,
		Set<String> tagList,
		@JsonFormat(shape = JsonFormat.Shape.STRING,
				pattern = "yyy-MM-dd'T'HH:mm:ss.SSS'Z'") OffsetDateTime createdAt,
		@JsonFormat(shape = JsonFormat.Shape.STRING,
				pattern = "yyy-MM-dd'T'HH:mm:ss.SSS'Z'") OffsetDateTime updatedAt,
		boolean favorited, int favoritesCount, Profile author) {
}
