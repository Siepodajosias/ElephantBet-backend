package com.eburtis.ElephantBet.controleur;

import com.eburtis.ElephantBet.facade.BilanMiseFacade;
import com.eburtis.ElephantBet.presentation.vo.BilanMisesVo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/bilan-mise")
public class BilanMiseController {

    private final BilanMiseFacade bilanMiseFacade;

    public BilanMiseController(BilanMiseFacade bilanMiseFacade) {
        this.bilanMiseFacade = bilanMiseFacade;
    }

    @GetMapping("/jour/{debut}/{fin}/{limite}")
    public ResponseEntity<Set<BilanMisesVo>> ListeBilanMisesPourJour(@PathVariable String debut, @PathVariable String fin, @PathVariable Integer limite) {
        try {
            Set<BilanMisesVo> bilans = bilanMiseFacade.listeBilanMisePourPeriode(debut, fin, limite);
            if (bilans.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(bilans, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/mois/{date}/{limite}")
    public ResponseEntity<Set<BilanMisesVo>> ListeBilanMisePourMois(@PathVariable String date, @PathVariable Integer limite) {
        try {
            Set<BilanMisesVo> bilans = bilanMiseFacade.listerBilanGainPourMois(date, limite);
            if (bilans.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(bilans, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/annee/{date}/{limite}")
    public ResponseEntity<Set<BilanMisesVo>> ListeBilanMisesPourAnnee(@PathVariable String date, @PathVariable Integer limite) {
        try {
            Set<BilanMisesVo> bilans = bilanMiseFacade.listerBilanMisesPourAnnee(date, limite);
            if (bilans.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(bilans, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
