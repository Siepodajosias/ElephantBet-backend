package com.eburtis.ElephantBet.controleur;

import com.eburtis.ElephantBet.exception.ResponseMessage;
import com.eburtis.ElephantBet.facade.BilanJeuFacade;
import com.eburtis.ElephantBet.presentation.vo.BilanJeuVo;
import com.eburtis.ElephantBet.service.servicehelper.ExcelHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping("/api/bilan-jeu")
public class BilanJeuController {

    private final BilanJeuFacade bilanJeuFacade;

    private final ExcelHelper excelHelper;

    public BilanJeuController(BilanJeuFacade bilanJeuFacade, ExcelHelper excelHelper) {
        this.bilanJeuFacade = bilanJeuFacade;
        this.excelHelper = excelHelper;
    }

    @PostMapping("/importer")
    public ResponseEntity<ResponseMessage> telechargerExcel(@RequestParam("file") MultipartFile file) throws IOException {
        String message = "";
        if (excelHelper.estFormatExcel(file)) {
            try {
                bilanJeuFacade.telechargementBilanJeu(file);
                message = "Fichier telechargé avec succès: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
                message = "Nous ne pouvons pas telecharger le fichier: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }
        message = "Svp telecharger un fichier excel!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }


    @GetMapping("/jour/{debut}/{fin}")
    public ResponseEntity<Set<BilanJeuVo>> ListeBilanJeuPourJour(@PathVariable String debut, @PathVariable String fin) {
        try {
            Set<BilanJeuVo> bilans = bilanJeuFacade.listeBilanJeuPourPeriode(debut, fin);
            if (bilans.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(bilans, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/mois/{date}")
    public ResponseEntity<Set<BilanJeuVo>> ListeBilanJeuPourMois(@PathVariable String date) {
        try {
            Set<BilanJeuVo> bilans = bilanJeuFacade.listerBilanJeuPourMois(date);
            if (bilans.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(bilans, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/annee/{date}")
    public ResponseEntity<Set<BilanJeuVo>> ListeBilanJeuPourAnnee(@PathVariable String date) {
        try {
            Set<BilanJeuVo> bilans = bilanJeuFacade.listerBilanJeuxPourAnnee(date);
            if (bilans.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(bilans, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
