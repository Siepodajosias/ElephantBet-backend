package com.eburtis.ElephantBet.configuration.securite;

import com.eburtis.ElephantBet.presentation.vo.UtilisateurVo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static io.jsonwebtoken.SignatureAlgorithm.HS512;
import static java.lang.System.currentTimeMillis;

@Component
public class JwtTokenUtils implements Serializable {

	private static final long serialVersionUID = -2550185165626007488L;

	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

	@Value("${jwt.secret}")
	private String secret;

	/**
	 * Retourne le nom de l'utilisateur compris dans le Token JWT.
	 *
	 * @param token Le Token JWT.
	 * @return le nom de l'utilisateur.
	 */
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	/**
	 * Retourne la date d'expiration du Token JWT.
	 *
	 * @param token Le Token JWT.
	 * @return La date d'expiration du Token JWT.
	 */
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	/**
	 * Retourne toutes les informations du Token JWT.
	 *
	 * @param token Le Token JWT.
	 * @return le nom de l'utilisateur.
	 */
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	/**
	 * Vérifie si le Token JWT a expiré.
	 *
	 * @param token Le Token JWT.
	 * @return true si le token a expiré.
	 */
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	/**
	 * Génère le Token JWT de le utilisateur rensigné.
	 *
	 * @param utilisateurVo le utilisateur.
	 * @return le Token JWT.
	 */
	public String generateToken(UtilisateurVo utilisateurVo) {
		Map<String, Object> claims = new HashMap<>();

		claims.put("id", utilisateurVo.getId());
		claims.put("nom", utilisateurVo.getNom());
		claims.put("prenoms", utilisateurVo.getPrenoms());
		claims.put("username", utilisateurVo.getUsername());
		claims.put("actif", utilisateurVo.isActif());
		claims.put("role", utilisateurVo.getRole());

		return doGenerateToken(claims, utilisateurVo.getUsername());
	}

	/**
	 * Permet de comptacter les attituts de l'utilisateur pour la génétaion du Token JWT.
	 *
	 * @param claims  Les attributs.
	 * @param subject le username de l'utilisateur.
	 * @return Les attributs formatés.
	 */
	private String doGenerateToken(Map<String, Object> claims, String subject) {

		return Jwts.builder().setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(currentTimeMillis()))
				.setExpiration(new Date(currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(HS512, secret).compact();
	}

	/**
	 * Permet de valider la correspondace entre les informations du Token JWT et celles d'un utilisateur.
	 *
	 * @param token       Le Token JWT.
	 * @param userDetails L'utilisateur.
	 * @return true si les informations corresponde et le Token JWT n'a pas expiré.
	 */
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}
