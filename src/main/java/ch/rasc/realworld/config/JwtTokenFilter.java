package ch.rasc.realworld.config;

import static ch.rasc.realworld.db.tables.AppUser.APP_USER;

import java.io.IOException;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenFilter extends OncePerRequestFilter {
	private final DSLContext dsl;

	private final JwtService jwtService;

	private final static String AUTHORIZATION_HEADER = "Authorization";
	private final static String TOKEN = "Token";

	public JwtTokenFilter(DSLContext dsl, JwtService jwtService) {
		this.dsl = dsl;
		this.jwtService = jwtService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String jwt = resolveToken(request);
		if (jwt != null) {
			Long userId = this.jwtService.getSubFromToken(jwt);
			if (userId != null) {
				var record = this.dsl.selectFrom(APP_USER).where(APP_USER.ID.eq(userId)).fetchOne();
				if (record != null) {
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
							new AuthenticatedUser(record), null, List.of());
					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
			}
		}

		filterChain.doFilter(request, response);
	}

	@SuppressWarnings("null")
	private static String resolveToken(HttpServletRequest request) {
		String token = request.getHeader(AUTHORIZATION_HEADER);
		if (StringUtils.hasText(token) && token.startsWith(TOKEN)) {
			return token.substring(TOKEN.length() + 1);
		}
		return null;
	}

}
