package ch.rasc.realworld.config;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.JWTVerifier;

@Component
public class JwtService {
	private final Algorithm algorithm;
	private final JWTVerifier verifier;

	@Autowired
	public JwtService() {
		byte[] secret = new byte[64];
		new SecureRandom().nextBytes(secret);
		this.algorithm = Algorithm.HMAC512(secret);
		this.verifier = JWT.require(this.algorithm).build();
	}

	public String toToken(long userId) {
		return JWT.create().withSubject(Long.toString(userId))
				.withExpiresAt(Instant.now().plus(1, ChronoUnit.DAYS))
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
