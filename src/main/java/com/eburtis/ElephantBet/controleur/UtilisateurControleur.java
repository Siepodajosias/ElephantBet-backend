package com.eburtis.ElephantBet.controleur;

import com.eburtis.ElephantBet.domain.Utilisateur;
import com.eburtis.ElephantBet.facade.UtilisateurFacade;
import com.eburtis.ElephantBet.presentation.vo.TokenVo;
import com.eburtis.ElephantBet.presentation.vo.UtilisateurVo;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurControleur {

    private final UtilisateurFacade utilisateurFacade;

    public UtilisateurControleur(UtilisateurFacade utilisateurFacade) {
        this.utilisateurFacade = utilisateurFacade;
    }

    @GetMapping
    public List<Utilisateur> listeUtilisateur() throws Exception {
        return utilisateurFacade.listeUtilisateurs();
    }

    @GetMapping("/{role}")
    public List<Utilisateur> listeUtilisateur(@PathVariable String role) throws Exception {
        return utilisateurFacade.listeUtilisateursParRole(role);
    }
    @PostMapping("/enregistrer")
    public void creerUtilisateur(@RequestBody @Valid UtilisateurVo utilisateurVo) throws Exception {
        utilisateurFacade.creerUtilisateur(utilisateurVo);
    }

    @PutMapping("/modifier")
    public Utilisateur modifierUtilisateur(@RequestBody @Valid UtilisateurVo utilisateurVo) throws Exception {
        return utilisateurFacade.modifierUtilisateur(utilisateurVo);
    }

    @DeleteMapping("/supprimer/{id}")
    public void supprimerUtilisateur(@PathVariable Long id) throws Exception {
        utilisateurFacade.supprimerUtilisateur(id);
    }

    @GetMapping("/count/{id}")
    public void countUtilisateur(@PathVariable Long id) throws Exception {
        utilisateurFacade.counterUtilisateur(id);
    }

}
