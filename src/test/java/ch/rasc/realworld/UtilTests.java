package ch.rasc.realworld;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;

import org.junit.jupiter.api.Test;

import ch.rasc.realworld.config.JwtService;

class UtilTests {

	@Test
	void createsCleanAndNonEmptySlugs() {
		assertThat(Util.toSlug("  Café & Spring!  ")).isEqualTo("cafe-spring");
		assertThat(Util.toSlug("?!")).isEqualTo("article");
		assertThat(Util.toSlug("日本 語")).isEqualTo("日本-語");
	}

	@Test
	void configuredJwtSecretWorksAcrossServiceInstances() {
		String secret = "a-repeatable-test-secret-with-enough-characters";
		JwtService issuer = new JwtService(secret, Duration.ofHours(1));
		JwtService verifier = new JwtService(secret, Duration.ofHours(1));

		assertThat(verifier.getSubFromToken(issuer.toToken(42))).isEqualTo(42L);
	}

}
