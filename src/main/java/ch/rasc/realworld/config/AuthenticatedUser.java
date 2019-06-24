package ch.rasc.realworld.config;

import ch.rasc.realworld.db.tables.records.AppUserRecord;

public class AuthenticatedUser {

	private final long id;
	private final String email;
	private final String username;
	private final String password;
	private final String bio;
	private final String image;

	public AuthenticatedUser(AppUserRecord record) {
		this.id = record.getId();
		this.email = record.getEmail();
		this.username = record.getUsername();
		this.password = record.getPassword();
		this.bio = record.getBio();
		this.image = record.getImage();
	}

	public long getId() {
		return this.id;
	}

	public String getEmail() {
		return this.email;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return this.password;
	}

	public String getBio() {
		return this.bio;
	}

	public String getImage() {
		return this.image;
	}

}
