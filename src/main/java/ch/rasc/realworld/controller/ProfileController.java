package ch.rasc.realworld.controller;

import static ch.rasc.realworld.db.tables.AppUser.APP_USER;
import static ch.rasc.realworld.db.tables.Follow.FOLLOW;

import java.util.Map;

import org.jooq.DSLContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.rasc.realworld.config.AuthenticatedUser;
import ch.rasc.realworld.dto.Profile;

@RestController
public class ProfileController {

	private final DSLContext dsl;

	public ProfileController(DSLContext dsl) {
		this.dsl = dsl;
	}

	@GetMapping("profiles/{username}")
	public ResponseEntity<?> getProfile(@PathVariable("username") String username,
			@AuthenticationPrincipal AuthenticatedUser user) {

		var userRecord = this.dsl.selectFrom(APP_USER)
				.where(APP_USER.USERNAME.eq(username)).fetchOne();
		if (userRecord == null) {
			return ResponseEntity.notFound().build();
		}

		boolean following = this.dsl.selectCount().from(FOLLOW)
				.where(FOLLOW.USER_ID.eq(user.getId())
						.and(FOLLOW.FOLLOW_ID.eq(userRecord.getId())))
				.fetchOne(0, int.class) == 1;

		Profile profile = new Profile(userRecord.getUsername(), userRecord.getBio(),
				userRecord.getImage(), following);

		return ResponseEntity.ok(Map.of("profile", profile));
	}

	@PostMapping(path = "profiles/{username}/follow")
	public ResponseEntity<?> follow(@PathVariable("username") String username,
			@AuthenticationPrincipal AuthenticatedUser user) {

		var userRecord = this.dsl.selectFrom(APP_USER)
				.where(APP_USER.USERNAME.eq(username)).fetchOne();
		if (userRecord == null) {
			return ResponseEntity.notFound().build();
		}

		this.dsl.insertInto(FOLLOW, FOLLOW.USER_ID, FOLLOW.FOLLOW_ID)
				.values(user.getId(), userRecord.getId()).execute();

		boolean following = this.dsl.selectCount().from(FOLLOW)
				.where(FOLLOW.USER_ID.eq(user.getId())
						.and(FOLLOW.FOLLOW_ID.eq(userRecord.getId())))
				.fetchOne(0, int.class) == 1;

		Profile profile = new Profile(userRecord.getUsername(), userRecord.getBio(),
				userRecord.getImage(), following);

		return ResponseEntity.ok(Map.of("profile", profile));
	}

	@DeleteMapping(path = "profiles/{username}/follow")
	public ResponseEntity<?> unfollow(@PathVariable("username") String username,
			@AuthenticationPrincipal AuthenticatedUser user) {
		var userRecord = this.dsl.selectFrom(APP_USER)
				.where(APP_USER.USERNAME.eq(username)).fetchOne();
		if (userRecord == null) {
			return ResponseEntity.notFound().build();
		}

		this.dsl.delete(FOLLOW).where(FOLLOW.USER_ID.eq(user.getId())
				.and(FOLLOW.FOLLOW_ID.eq(userRecord.getId()))).execute();

		boolean following = this.dsl.selectCount().from(FOLLOW)
				.where(FOLLOW.USER_ID.eq(user.getId())
						.and(FOLLOW.FOLLOW_ID.eq(userRecord.getId())))
				.fetchOne(0, int.class) == 1;

		Profile profile = new Profile(userRecord.getUsername(), userRecord.getBio(),
				userRecord.getImage(), following);

		return ResponseEntity.ok(Map.of("profile", profile));
	}

}
