package ch.rasc.realworld.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record Comment(long id,
		@JsonFormat(shape = JsonFormat.Shape.STRING,
				pattern = "yyy-MM-dd'T'HH:mm:ss.SSS'Z'") LocalDateTime createdAt,
		@JsonFormat(shape = JsonFormat.Shape.STRING,
				pattern = "yyy-MM-dd'T'HH:mm:ss.SSS'Z'") LocalDateTime updatedAt,
		String body, Profile author) {

}
