package ch.rasc.realworld.dto;

import ch.rasc.realworld.db.tables.records.AppUserRecord;

public class User {

	private final String email;
	private final String token;
	private final String username;
	private final String bio;
	private final String image;

	public User(AppUserRecord record, String token) {
		this.email = record.getEmail();
		this.username = record.getUsername();
		this.bio = record.getBio();
		this.image = record.getImage();
		this.token = token;
	}

	public String getEmail() {
		return this.email;
	}

	public String getToken() {
		return this.token;
	}

	public String getUsername() {
		return this.username;
	}

	public String getBio() {
		return this.bio;
	}

	public String getImage() {
		return this.image;
	}

}
