package ch.rasc.realworld.controller;

import static ch.rasc.realworld.db.tables.AppUser.APP_USER;

import java.util.Map;

import org.jooq.DSLContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonRootName;

import ch.rasc.realworld.Util;
import ch.rasc.realworld.config.JwtService;
import ch.rasc.realworld.db.tables.records.AppUserRecord;
import ch.rasc.realworld.dto.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@RestController
public class UsersController {

	private final DSLContext dsl;

	private final PasswordEncoder passwordEncoder;

	private final JwtService jwtService;

	public UsersController(DSLContext dsl, PasswordEncoder passwordEncoder,
			JwtService jwtService) {
		this.dsl = dsl;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	@SuppressWarnings("null")
	@PostMapping("/users")
	public ResponseEntity<?> createUser(@Valid @RequestBody RegisterParam registerParam,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
					.body(Util.toError(bindingResult));
		}

		if (this.dsl.selectCount().from(APP_USER)
				.where(APP_USER.USERNAME.eq(registerParam.username()))
				.fetchOne(0, int.class) == 1) {
			bindingResult.rejectValue("username", "DUPLICATED", "duplicated username");
		}

		if (this.dsl.selectCount().from(APP_USER)
				.where(APP_USER.EMAIL.eq(registerParam.email()))
				.fetchOne(0, int.class) == 1) {
			bindingResult.rejectValue("email", "DUPLICATED", "duplicated email");
		}

		if (bindingResult.hasErrors()) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
					.body(Util.toError(bindingResult));
		}

		AppUserRecord newRecord = this.dsl.newRecord(APP_USER);
		newRecord.setEmail(registerParam.email());
		newRecord.setUsername(registerParam.username());
		newRecord.setPassword(this.passwordEncoder.encode(registerParam.password()));
		newRecord.setImage(registerParam.image());
		newRecord.setBio(registerParam.bio());
		newRecord.store();

		String token = this.jwtService.toToken(newRecord.getId());
		User user = new User(newRecord.getEmail(), token, newRecord.getUsername(),
				newRecord.getBio(), newRecord.getImage());
		return ResponseEntity.status(201).body(Map.of("user", user));
	}

	@PostMapping("/users/login")
	public ResponseEntity<?> userLogin(@Valid @RequestBody LoginParam loginParam,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
					.body(Util.toError(bindingResult));
		}

		var userRecord = this.dsl.selectFrom(APP_USER)
				.where(APP_USER.EMAIL.eq(loginParam.email())).fetchOne();
		if (userRecord != null && this.passwordEncoder.matches(loginParam.password(),
				userRecord.getPassword())) {
			String token = this.jwtService.toToken(userRecord.getId());
			User user = new User(userRecord.getEmail(), token, userRecord.getUsername(),
					userRecord.getBio(), userRecord.getImage());
			return ResponseEntity.ok(Map.of("user", user));
		}
		bindingResult.rejectValue("password", "INVALID", "invalid email or password");
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(Util.toError(bindingResult));
	}

}

@JsonRootName("user")
record LoginParam(
		@NotBlank(message = "can't be empty") @Email(
				message = "should be an email") String email,
		@NotBlank(message = "can't be empty") String password) {
}

@JsonRootName("user")
record RegisterParam(
		@NotBlank(message = "can't be empty") @Email(
				message = "should be an email") String email,
		String bio, String image, @NotBlank(message = "can't be empty") String username,
		@NotBlank(message = "can't be empty") String password) {
}