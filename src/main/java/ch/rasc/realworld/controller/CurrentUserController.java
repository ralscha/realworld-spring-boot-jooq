package ch.rasc.realworld.controller;

import static ch.rasc.realworld.db.tables.AppUser.APP_USER;

import java.util.Map;

import org.jooq.DSLContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
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
import ch.rasc.realworld.config.JwtService;
import ch.rasc.realworld.dto.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@RestController
public class CurrentUserController {

	private final DSLContext dsl;

	private final PasswordEncoder passwordEncoder;

	private final JwtService jwtService;

	public CurrentUserController(DSLContext dsl, PasswordEncoder passwordEncoder, JwtService jwtService) {
		this.dsl = dsl;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	@GetMapping("/user")
	public ResponseEntity<?> currentUser(@AuthenticationPrincipal AuthenticatedUser currentUser) {
		var userRecord = this.dsl.selectFrom(APP_USER).where(APP_USER.ID.eq(currentUser.getId())).fetchOne();
		User user = new User(userRecord.getEmail(), this.jwtService.toToken(userRecord.getId()),
				userRecord.getUsername(), userRecord.getBio(), userRecord.getImage());
		return ResponseEntity.ok().body(Map.of("user", user));
	}

	@PutMapping("/user")
	public ResponseEntity<?> updateProfile(@AuthenticationPrincipal AuthenticatedUser currentUser,
			@Valid @RequestBody UpdateUserParam updateUserParam, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Util.toError(bindingResult));
		}

		if (updateUserParam.username != null && this.dsl.fetchExists(APP_USER,
				APP_USER.USERNAME.eq(updateUserParam.username).and(APP_USER.ID.ne(currentUser.getId())))) {
			bindingResult.rejectValue("username", "DUPLICATED", "duplicated username");
		}

		if (updateUserParam.email != null && this.dsl.fetchExists(APP_USER,
				APP_USER.EMAIL.eq(updateUserParam.email).and(APP_USER.ID.ne(currentUser.getId())))) {
			bindingResult.rejectValue("email", "DUPLICATED", "duplicated email");
		}

		if (bindingResult.hasErrors()) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(Util.toError(bindingResult));
		}

		var userRecord = this.dsl.selectFrom(APP_USER).where(APP_USER.ID.eq(currentUser.getId())).fetchOne();
		if (updateUserParam.email != null) {
			userRecord.setEmail(updateUserParam.email);
		}
		if (StringUtils.hasText(updateUserParam.password)) {
			userRecord.setPassword(this.passwordEncoder.encode(updateUserParam.password));
		}
		if (updateUserParam.username != null) {
			userRecord.setUsername(updateUserParam.username);
		}
		if (updateUserParam.bioPresent) {
			userRecord.setBio(updateUserParam.bio);
		}
		if (updateUserParam.imagePresent) {
			userRecord.setImage(updateUserParam.image);
		}
		userRecord.update();

		User user = new User(userRecord.getEmail(), this.jwtService.toToken(userRecord.getId()),
				userRecord.getUsername(), userRecord.getBio(), userRecord.getImage());
		return ResponseEntity.ok().body(Map.of("user", user));
	}

}

@JsonRootName("user")
@JsonAutoDetect(fieldVisibility = Visibility.NONE, setterVisibility = Visibility.ANY)
class UpdateUserParam {

	@Email(message = "should be an email")
	@Pattern(regexp = "(?s).*\\S.*", message = "can't be empty")
	@Size(max = 255, message = "is too long")
	String email;

	@Pattern(regexp = "(?s).*\\S.*", message = "can't be empty")
	String password;

	@Pattern(regexp = "(?s).*\\S.*", message = "can't be empty")
	@Size(max = 255, message = "is too long")
	String username;

	String bio;

	String image;

	boolean bioPresent;

	boolean imagePresent;

	void setEmail(String email) {
		this.email = email;
	}

	void setPassword(String password) {
		this.password = password;
	}

	void setUsername(String username) {
		this.username = username;
	}

	void setBio(String bio) {
		this.bio = bio;
		this.bioPresent = true;
	}

	void setImage(String image) {
		this.image = image;
		this.imagePresent = true;
	}

}
