package ch.rasc.realworld.config;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;

@Component
public class JwtService {

	private final Algorithm algorithm;

	private final JWTVerifier verifier;

	private final Duration expiration;

	public JwtService(@Value("${realworld.jwt.secret}") String secret,
			@Value("${realworld.jwt.expiration:PT24H}") Duration expiration) {
		if (secret.length() < 32) {
			throw new IllegalArgumentException("realworld.jwt.secret must contain at least 32 characters");
		}
		if (expiration.isNegative() || expiration.isZero()) {
			throw new IllegalArgumentException("realworld.jwt.expiration must be positive");
		}
		this.algorithm = Algorithm.HMAC512(secret.getBytes(StandardCharsets.UTF_8));
		this.verifier = JWT.require(this.algorithm).build();
		this.expiration = expiration;
	}

	public String toToken(long userId) {
		return JWT.create()
			.withSubject(Long.toString(userId))
			.withExpiresAt(Instant.now().plus(this.expiration))
			.sign(this.algorithm);
	}

	public Long getSubFromToken(String token) {
		try {
			return Long.valueOf(this.verifier.verify(token).getSubject());
		}
		catch (Exception e) {
			return null;
		}
	}

}
