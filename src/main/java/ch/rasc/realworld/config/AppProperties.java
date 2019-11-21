package ch.rasc.realworld.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("app")
@Component
public class AppProperties {

	private String jwtSecret;

	private Duration jwtValidity;

	private String defaultImage;

	public String getJwtSecret() {
		return this.jwtSecret;
	}

	public void setJwtSecret(String jwtSecret) {
		this.jwtSecret = jwtSecret;
	}

	public Duration getJwtValidity() {
		return this.jwtValidity;
	}

	public void setJwtValidity(Duration jwtValidity) {
		this.jwtValidity = jwtValidity;
	}

	public String getDefaultImage() {
		return this.defaultImage;
	}

	public void setDefaultImage(String defaultImage) {
		this.defaultImage = defaultImage;
	}

}
