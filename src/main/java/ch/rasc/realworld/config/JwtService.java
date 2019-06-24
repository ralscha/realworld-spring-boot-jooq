package ch.rasc.realworld.config;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtService {
	private final AppProperties appProperties;

	@Autowired
	public JwtService(AppProperties appProperties) {
		this.appProperties = appProperties;
	}

	public String toToken(long userId) {
		return Jwts.builder().setSubject(Long.toString(userId))
				.setExpiration(expireTimeFromNow())
				.signWith(SignatureAlgorithm.HS512, this.appProperties.getJwtSecret())
				.compact();
	}

	public Long getSubFromToken(String token) {
		try {
			Jws<Claims> claimsJws = Jwts.parser()
					.setSigningKey(this.appProperties.getJwtSecret())
					.parseClaimsJws(token);
			return Long.valueOf(claimsJws.getBody().getSubject());
		}
		catch (Exception e) {
			return null;
		}
	}

	private Date expireTimeFromNow() {
		LocalDateTime ld = LocalDateTime.now().plus(this.appProperties.getJwtValidity());
		return java.util.Date.from(ld.atZone(ZoneId.systemDefault()).toInstant());
	}
}
