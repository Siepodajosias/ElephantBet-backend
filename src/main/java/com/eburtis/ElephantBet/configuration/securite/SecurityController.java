package com.eburtis.ElephantBet.configuration.securite;

import com.eburtis.ElephantBet.presentation.vo.UsernamePasswordVo;
import com.eburtis.ElephantBet.presentation.vo.TokenVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/securite")
public class SecurityController {

	private final SecurityService securityService;

	public SecurityController(SecurityService securityService) {
		this.securityService = securityService;
	}

	/**
	 * Permet d'authentifier l'utilisateur.
	 *
	 * @param usernamePasswordVo les informations de connexion.
	 * @return le token d'authentification
	 */
	@PostMapping("/auth")
	public TokenVo auth(@RequestBody UsernamePasswordVo usernamePasswordVo) {
		return securityService.authentifierUtilisateur(usernamePasswordVo);
	}
}
