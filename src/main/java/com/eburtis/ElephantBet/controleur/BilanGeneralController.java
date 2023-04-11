package com.eburtis.ElephantBet.controleur;

import com.eburtis.ElephantBet.facade.BilanGeneralFacade;
import com.eburtis.ElephantBet.presentation.vo.BilanGeneralVo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;


/**
 * Controleur pour tous les traiatements sur le bilan général.
 */
@RestController
@RequestMapping("/api/bilan-general")
public class BilanGeneralController {

    private final BilanGeneralFacade bilanGenreralFacade;

    public BilanGeneralController(BilanGeneralFacade bilanFacade) {
        this.bilanGenreralFacade = bilanFacade;
    }

    /**
     * Recupére tous les bilans pour une journée.
     *
     * @param dateDebut la date de début de la période.
     * @param dateFin   la date de fin de la période.
     * @return Une liste de bilan général pour une période.
     */
    @GetMapping("/jour/{dateDebut}/{dateFin}")
    public ResponseEntity<Set<BilanGeneralVo>> listerBilanPourPeriode(@PathVariable String dateDebut, @PathVariable String dateFin) {
        try {
            Set<BilanGeneralVo> bilans = bilanGenreralFacade.listerBilanGeneralPourPeriode(dateDebut, dateFin);
            if (bilans.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(bilans, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Recupére tous les bilans pour un mois passé en paramètre.
     *
     * @param mois Le mois concerné.
     * @return Une liste de bilan général pour un mois.
     */
    @GetMapping("/mois/{mois}")
    public ResponseEntity<Set<BilanGeneralVo>> listerBilanPourMois(@PathVariable String mois) {
        try {
            Set<BilanGeneralVo> bilans = bilanGenreralFacade.listeBilanGeneralPourMois(mois);
            if (bilans.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(bilans, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/annee/{annee}")
    public ResponseEntity<Set<BilanGeneralVo>> listerBilanGeneralePourUneAnnee(@PathVariable String annee) {
        try {
            Set<BilanGeneralVo> bilans = bilanGenreralFacade.listeBilanGeneralPourAnnee(annee);
            if (bilans.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(bilans, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
