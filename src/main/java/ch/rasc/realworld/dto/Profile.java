package ch.rasc.realworld.dto;

public class Profile {
	private final String username;

	private final String bio;

	private final String image;

	private final boolean following;

	public Profile(String username, String bio, String image, boolean following) {
		this.username = username;
		this.bio = bio;
		this.image = image;
		this.following = following;
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

	public boolean isFollowing() {
		return this.following;
	}

}
