package ch.rasc.realworld.config;

import static java.util.Arrays.asList;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class WebSecurityConfig {

	private final JwtTokenFilter jwtTokenFilter;

	public WebSecurityConfig(DSLContext dsl, JwtService jwtService) {
		this.jwtTokenFilter = new JwtTokenFilter(dsl, jwtService);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf().disable().cors().and().exceptionHandling()
				.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeHttpRequests()
				.requestMatchers(HttpMethod.POST, "/users").permitAll().requestMatchers(HttpMethod.POST, "/users/login")
				.permitAll().requestMatchers(HttpMethod.GET, "/tags").permitAll()
				.requestMatchers(HttpMethod.GET, "/profiles/*").permitAll().requestMatchers(HttpMethod.GET, "/articles")
				.permitAll().requestMatchers(HttpMethod.GET, "/articles/feed").authenticated()
				.requestMatchers(HttpMethod.GET, "/articles/*").permitAll()
				.requestMatchers(HttpMethod.GET, "/articles/*/comments").permitAll()

				.requestMatchers(HttpMethod.PUT, "/user").authenticated()
				.requestMatchers(HttpMethod.POST, "/profiles/*/follow").authenticated()
				.requestMatchers(HttpMethod.DELETE, "/profiles/*/follow").authenticated()
				.requestMatchers(HttpMethod.POST, "/articles").authenticated()
				.requestMatchers(HttpMethod.PUT, "/articles/*").authenticated()
				.requestMatchers(HttpMethod.DELETE, "/articles/*").authenticated()
				.requestMatchers(HttpMethod.POST, "/articles/*/comments").authenticated()
				.requestMatchers(HttpMethod.DELETE, "/articles/*/comments/*").authenticated()
				.requestMatchers(HttpMethod.POST, "/articles/*/favorite").authenticated()
				.requestMatchers(HttpMethod.DELETE, "/articles/*/favorite").authenticated()
				.requestMatchers(HttpMethod.GET, "/user").authenticated()

				.anyRequest().authenticated().and()
				.addFilterBefore(this.jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@SuppressWarnings("null")
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("*"));
		configuration.setAllowedMethods(asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
		// setAllowCredentials(true) is important, otherwise:
		// The value of the 'Access-Control-Allow-Origin' header in the response must
		// not
		// be the wildcard '*' when the request's credentials mode is 'include'.
		configuration.setAllowCredentials(true);
		// setAllowedHeaders is important! Without it, OPTIONS preflight request
		// will fail with 403 Invalid CORS request
		configuration.setAllowedHeaders(asList("Authorization", "Cache-Control", "Content-Type"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
