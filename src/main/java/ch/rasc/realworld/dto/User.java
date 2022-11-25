package ch.rasc.realworld.dto;

public record User(String email, String token, String username, String bio,
		String image) {
}
