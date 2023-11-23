package ch.rasc.realworld.config;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

@Component
public class JwtService {
	private final SecretKey key;
	private final JwtParser jwtParser;

	@Autowired
	public JwtService() {
		this.key = Jwts.SIG.HS512.key().build();
		this.jwtParser = Jwts.parser().verifyWith(this.key).build();
	}

	public String toToken(long userId) {
		return Jwts.builder().signWith(this.key).subject(Long.toString(userId))
				.expiration(expireTimeFromNow()).compact();
	}

	public Long getSubFromToken(String token) {
		try {
			Jws<Claims> claimsJws = this.jwtParser.parseSignedClaims(token);
			return Long.valueOf(claimsJws.getPayload().getSubject());
		}
		catch (Exception e) {
			return null;
		}
	}

	private static Date expireTimeFromNow() {
		LocalDateTime ld = LocalDateTime.now().plus(1, ChronoUnit.DAYS);
		return java.util.Date.from(ld.atZone(ZoneId.systemDefault()).toInstant());
	}
}
