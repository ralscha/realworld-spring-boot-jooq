package ch.rasc.realworld.controller;

import static ch.rasc.realworld.db.tables.AppUser.APP_USER;

import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.jooq.DSLContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonRootName;

import ch.rasc.realworld.Util;
import ch.rasc.realworld.config.AppProperties;
import ch.rasc.realworld.config.JwtService;
import ch.rasc.realworld.db.tables.records.AppUserRecord;
import ch.rasc.realworld.dto.User;

@RestController
public class UsersController {

	private final DSLContext dsl;

	private final PasswordEncoder passwordEncoder;

	private final AppProperties appProperties;

	private final JwtService jwtService;

	public UsersController(DSLContext dsl, PasswordEncoder passwordEncoder,
			AppProperties appProperties, JwtService jwtService) {
		this.dsl = dsl;
		this.passwordEncoder = passwordEncoder;
		this.appProperties = appProperties;
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
				.where(APP_USER.USERNAME.eq(registerParam.username))
				.fetchOne(0, int.class) == 1) {
			bindingResult.rejectValue("username", "DUPLICATED", "duplicated username");
		}

		if (this.dsl.selectCount().from(APP_USER)
				.where(APP_USER.EMAIL.eq(registerParam.email))
				.fetchOne(0, int.class) == 1) {
			bindingResult.rejectValue("email", "DUPLICATED", "duplicated email");
		}

		if (bindingResult.hasErrors()) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
					.body(Util.toError(bindingResult));
		}

		AppUserRecord newRecord = this.dsl.newRecord(APP_USER);
		newRecord.setEmail(registerParam.email);
		newRecord.setUsername(registerParam.username);
		newRecord.setPassword(this.passwordEncoder.encode(registerParam.password));
		newRecord.setImage(this.appProperties.getDefaultImage());
		newRecord.setBio("");
		newRecord.store();

		User user = new User(newRecord, this.jwtService.toToken(newRecord.getId()));
		return ResponseEntity.status(201).body(Map.of("user", user));
	}

	@SuppressWarnings("null")
	@PostMapping("/users/login")
	public ResponseEntity<?> userLogin(@Valid @RequestBody LoginParam loginParam,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
					.body(Util.toError(bindingResult));
		}

		var userRecord = this.dsl.selectFrom(APP_USER)
				.where(APP_USER.EMAIL.eq(loginParam.email)).fetchOne();
		if (userRecord != null && this.passwordEncoder.matches(loginParam.password,
				userRecord.getPassword())) {
			User user = new User(userRecord, this.jwtService.toToken(userRecord.getId()));
			return ResponseEntity.ok(Map.of("user", user));
		}
		bindingResult.rejectValue("password", "INVALID", "invalid email or password");
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(Util.toError(bindingResult));
	}

}

@JsonRootName("user")
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
class LoginParam {
	@NotBlank(message = "can't be empty")
	@Email(message = "should be an email")
	String email;
	@NotBlank(message = "can't be empty")
	String password;
}

@JsonRootName("user")
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
class RegisterParam {
	@NotBlank(message = "can't be empty")
	@Email(message = "should be an email")
	String email;
	
	@NotBlank(message = "can't be empty")
	String username;
	
	@NotBlank(message = "can't be empty")	
	String password;	
}