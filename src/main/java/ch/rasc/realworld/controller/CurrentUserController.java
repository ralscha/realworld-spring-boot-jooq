package ch.rasc.realworld.controller;

import static ch.rasc.realworld.db.tables.AppUser.APP_USER;

import java.util.Map;

import org.jooq.DSLContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonRootName;

import ch.rasc.realworld.Util;
import ch.rasc.realworld.config.AuthenticatedUser;
import ch.rasc.realworld.dto.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;

@RestController
public class CurrentUserController {
	private final DSLContext dsl;

	public CurrentUserController(DSLContext dsl) {
		this.dsl = dsl;
	}

	@GetMapping("/user")
	public ResponseEntity<?> currentUser(@AuthenticationPrincipal AuthenticatedUser currentUser) {
		var userRecord = this.dsl.selectFrom(APP_USER).where(APP_USER.ID.eq(currentUser.getId())).fetchOne();
		User user = new User(userRecord.getEmail(), "", userRecord.getUsername(), userRecord.getBio(),
				userRecord.getImage());
		return ResponseEntity.ok().body(Map.of("user", user));
	}

	@PutMapping("/user")
	public ResponseEntity<?> updateProfile(@AuthenticationPrincipal AuthenticatedUser currentUser,
			@Valid @RequestBody UpdateUserParam updateUserParam, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Util.toError(bindingResult));
		}

		if (this.dsl.selectCount().from(APP_USER)
				.where(APP_USER.USERNAME.eq(updateUserParam.username).and(APP_USER.ID.ne(currentUser.getId())))
				.fetchOne(0, int.class) == 1) {
			bindingResult.rejectValue("username", "DUPLICATED", "duplicated username");
		}

		if (this.dsl.selectCount().from(APP_USER)
				.where(APP_USER.EMAIL.eq(updateUserParam.email).and(APP_USER.ID.ne(currentUser.getId())))
				.fetchOne(0, int.class) == 1) {
			bindingResult.rejectValue("email", "DUPLICATED", "duplicated email");
		}

		if (bindingResult.hasErrors()) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Util.toError(bindingResult));
		}

		this.dsl.update(APP_USER).set(APP_USER.EMAIL, updateUserParam.email)
				.set(APP_USER.USERNAME, updateUserParam.username).set(APP_USER.BIO, updateUserParam.bio)
				.set(APP_USER.IMAGE, updateUserParam.image).where(APP_USER.ID.eq(currentUser.getId())).execute();

		var userRecord = this.dsl.selectFrom(APP_USER).where(APP_USER.ID.eq(currentUser.getId())).fetchOne();

		User user = new User(userRecord.getEmail(), "", userRecord.getUsername(), userRecord.getBio(),
				userRecord.getImage());
		return ResponseEntity.ok().body(Map.of("user", user));
	}

}

@JsonRootName("user")
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
class UpdateUserParam {
	@Email(message = "should be an email")
	String email = "";
	String password = "";
	String username = "";
	String bio = "";
	String image = "";
}
