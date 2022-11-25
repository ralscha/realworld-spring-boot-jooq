package ch.rasc.realworld.dto;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record Comment(long id,
		@JsonFormat(shape = JsonFormat.Shape.STRING,
				pattern = "yyy-MM-dd'T'HH:mm:ss.SSS'Z'") OffsetDateTime createdAt,
		@JsonFormat(shape = JsonFormat.Shape.STRING,
				pattern = "yyy-MM-dd'T'HH:mm:ss.SSS'Z'") OffsetDateTime updatedAt,
		String body, Profile author) {

}
