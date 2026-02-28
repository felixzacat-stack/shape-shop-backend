package com.shapeshop.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.shapeshop.service.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private UserService userDetailsService;

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String authorizationHeader = request.getHeader("Authorization");

		String username = null;
		String jwt = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwt = authorizationHeader.substring(7);

			try {
				username = JwtUtil.extractClaim(jwt, Claims::getSubject);
			} catch (ExpiredJwtException e) {
				response.sendError(500, "Expired JWT");
			} catch (SignatureException e) {
				response.sendError(500, "SignatureException. JWT signature does not match locally computed signature");
			} catch (Exception e) {
				response.sendError(500, "Exception. JWT exception " + e.getMessage());
			}
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = userDetailsService.loadUserByUsername(username);

			if (jwtUtil.validateToken(jwt, userDetails)) {

				UsernamePasswordAuthenticationToken authentication = jwtUtil.getAuthentication(jwt,
						SecurityContextHolder.getContext().getAuthentication(), userDetails);

				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				logger.info("authenticated user " + username + ", setting security context");
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		chain.doFilter(request, response);
	}
}
