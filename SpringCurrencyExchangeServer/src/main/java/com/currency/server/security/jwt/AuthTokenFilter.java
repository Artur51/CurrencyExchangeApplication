package com.currency.server.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.currency.server.services.UserRegistrationService;

public class AuthTokenFilter extends OncePerRequestFilter {

    public static final String HEADER_NAME = "Authorization";
    public static final String HEADER_PREFIX = "Bearer ";

	@Autowired
	private JsonTokenGenerator jwtUtils;

	@Autowired
    private UserRegistrationService userRegistrationService;

	private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			String jwt = parseJwt(request);
			if (jwt != null && jwtUtils.validateJsonToken(jwt)) {
				String username = jwtUtils.getUserNameFromJsonToken(jwt);

                UserDetails userDetails = userRegistrationService.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception e) {
			logger.error("Cannot set user authentication: {}", e);
		}

		filterChain.doFilter(request, response);
	}

	private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(HEADER_NAME);

		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith(HEADER_PREFIX)) {
			return headerAuth.substring(HEADER_PREFIX.length(), headerAuth.length());
		}

		return null;
	}
}
