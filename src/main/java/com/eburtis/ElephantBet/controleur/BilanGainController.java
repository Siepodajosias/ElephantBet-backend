package com.eburtis.ElephantBet.controleur;

import com.eburtis.ElephantBet.facade.BilanGainFacade;
import com.eburtis.ElephantBet.presentation.vo.BilanGainsVo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/bilan-gain")
public class BilanGainController {

    private final BilanGainFacade bilanGainFacade;

    public BilanGainController(BilanGainFacade bilanGainFacade) {
        this.bilanGainFacade = bilanGainFacade;
    }


    @GetMapping("/jour/{debut}/{fin}/{limite}")
    public ResponseEntity<Set<BilanGainsVo>> listerBilanGainsPourPeriode(@PathVariable String debut, @PathVariable String fin, @PathVariable Integer limite) {
        try {
            Set<BilanGainsVo> bilansGains = bilanGainFacade.listeBilanGainsPourPeriode(debut, fin, limite);
            if (bilansGains.isEmpty()) {
                return new ResponseEntity<>(NO_CONTENT);
            }
            return new ResponseEntity<>(bilansGains, OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/mois/{date}/{limite}")
    public ResponseEntity<Set<BilanGainsVo>> listerBilanGainsPourMois(@PathVariable String date, @PathVariable Integer limite) {
        try {
            Set<BilanGainsVo> bilansGains = bilanGainFacade.listerBilanGainsPourMois(date, limite);
            if (bilansGains.isEmpty()) {
                return new ResponseEntity<>(NO_CONTENT);
            }
            return new ResponseEntity<>(bilansGains, OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/annee/{date}/{limite}")
    public ResponseEntity<Set<BilanGainsVo>> listerBilanGainsPourAnnee(@PathVariable String date, @PathVariable Integer limite) {
        try {
            Set<BilanGainsVo> bilans = bilanGainFacade.listeBilanGainsPourAnnee(date, limite);
            if (bilans.isEmpty()) {
                return new ResponseEntity<>(NO_CONTENT);
            }
            return new ResponseEntity<>(bilans, OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, INTERNAL_SERVER_ERROR);
        }
    }
}
