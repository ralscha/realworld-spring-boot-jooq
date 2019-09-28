package ch.rasc.realworld.config;

import static java.util.Arrays.asList;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final boolean h2ConsoleEnabled;

	private final JwtTokenFilter jwtTokenFilter;

	public WebSecurityConfig(@Value("${spring.h2.console.enabled:false}") boolean h2ConsoleEnabled,
			DSLContext dsl, JwtService jwtService) {
		this.h2ConsoleEnabled = h2ConsoleEnabled;
		this.jwtTokenFilter = new JwtTokenFilter(dsl, jwtService);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		if (this.h2ConsoleEnabled) {
			http.authorizeRequests().antMatchers("/h2-console", "/h2-console/**")
					.permitAll().and().headers().frameOptions().sameOrigin();
		}

		http.csrf().disable().cors().and().exceptionHandling()
				.authenticationEntryPoint(
						new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
				.and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.authorizeRequests()
				.antMatchers(HttpMethod.GET, "/articles/feed").authenticated()
				.antMatchers(HttpMethod.POST, "/users", "/users/login").permitAll()
				.antMatchers(HttpMethod.GET, "/articles/**", "/profiles/**", "/tags")
				.permitAll().anyRequest().authenticated().and().addFilterBefore(
						this.jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@SuppressWarnings("null")
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("*"));
		configuration.setAllowedMethods(
				asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
		// setAllowCredentials(true) is important, otherwise:
		// The value of the 'Access-Control-Allow-Origin' header in the response must not
		// be the wildcard '*' when the request's credentials mode is 'include'.
		configuration.setAllowCredentials(true);
		// setAllowedHeaders is important! Without it, OPTIONS preflight request
		// will fail with 403 Invalid CORS request
		configuration.setAllowedHeaders(
				asList("Authorization", "Cache-Control", "Content-Type"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
