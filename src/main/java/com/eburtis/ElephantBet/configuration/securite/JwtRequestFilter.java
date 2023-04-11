package com.eburtis.ElephantBet.configuration.securite;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	private final SecurityService securityService;

	private final JwtTokenUtils jwtTokenUtils;

	public JwtRequestFilter(SecurityService securityService, JwtTokenUtils jwtTokenUtils) {
		this.securityService = securityService;
		this.jwtTokenUtils = jwtTokenUtils;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String requestTokenHeader = request.getHeader("Authorization");

		String username = null;
		String jwtToken = null;

		// Le Token JWT est constitué comme suit: "Bearer + token". Il faut retirer le mot "Bearer " pour conserver uniquement le token.
		if (requestTokenHeader != null) {
			if (requestTokenHeader.startsWith("Bearer ")) {
				jwtToken = requestTokenHeader.substring(7);
				try {
					username = jwtTokenUtils.getUsernameFromToken(jwtToken);
				} catch (IllegalArgumentException e) {
					System.out.println("Impossible de récupérer le token JWT");
				} catch (ExpiredJwtException e) {
					System.out.println("Le Token JWT a expiré");
				}
			} else {
				logger.warn("Le Token JWT est malformé: il ne commence pas par Bearer");
			}
		}

		// Une fois le Token JWT récupéré, on procède à sa validation.
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

			// Récupération de l'utilisateur
			UserDetails userDetails = this.securityService.loadUserByUsername(username);

			// Si le Token JWT est valide, on renseigne manuellemnt les informations de l'utilisateur authentifié
			if (jwtTokenUtils.validateToken(jwtToken, userDetails)) {
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());

				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource()
								.buildDetails(request));

				// Après avoir renseigné les informations de l'utilisateur, on spécifie que celui-ci est authentifié.
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		chain.doFilter(request, response);
	}
}
