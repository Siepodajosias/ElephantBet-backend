package com.eburtis.ElephantBet.presentation.factory;

import com.eburtis.ElephantBet.domain.Utilisateur;
import com.eburtis.ElephantBet.presentation.vo.UtilisateurVo;
import org.springframework.stereotype.Service;

/**
 * Fabrique des utilisateurs vo
 */
@Service
public class UtilisateurVoFactory {

    /**
     * Retourne un utilisateur Vo
     *
     * @param utilisateur l'utilisateur
     * @return un utilisateur Vo.
     */
    public UtilisateurVo utilisateurVo(Utilisateur utilisateur) {
        return new UtilisateurVo(utilisateur);
    }
}
