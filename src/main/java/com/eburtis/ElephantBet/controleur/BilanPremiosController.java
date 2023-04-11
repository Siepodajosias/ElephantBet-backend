package com.eburtis.ElephantBet.controleur;

import com.eburtis.ElephantBet.exception.ResponseMessage;
import com.eburtis.ElephantBet.facade.BilanPremiosFacade;
import com.eburtis.ElephantBet.presentation.vo.BilanPremiosVo;
import com.eburtis.ElephantBet.service.servicehelper.ExcelHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping("/api/bilan-premios")
public class BilanPremiosController {


    private final BilanPremiosFacade bilanPremiosFacade;
    private final ExcelHelper excelHelper;

    public BilanPremiosController(BilanPremiosFacade bilanPremiosFacade, ExcelHelper excelHelper) {
        this.bilanPremiosFacade = bilanPremiosFacade;
        this.excelHelper = excelHelper;
    }

    @PostMapping("/importer")
    public ResponseEntity<ResponseMessage> telechargerExcel(@RequestParam("file") MultipartFile file) throws IOException {
        String message = "";
        if (excelHelper.estFormatExcel(file)) {
            try {
                bilanPremiosFacade.telechargementBilanPremios(file);
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
    public ResponseEntity<Set<BilanPremiosVo>> ListeBilanPremiosPourJour(@PathVariable String debut, @PathVariable String fin) {
        try {
            Set<BilanPremiosVo> bilans = bilanPremiosFacade.listeBilanPremiosPourPeriode(debut, fin);
            if (bilans.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(bilans, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/mois/{date}")
    public ResponseEntity<Set<BilanPremiosVo>> ListeBilanPremiosPourMois(@PathVariable String date) {
        try {
            Set<BilanPremiosVo> bilans = bilanPremiosFacade.listerBilanPremiosPourMois(date);
            if (bilans.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(bilans, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/annee/{date}")
    public ResponseEntity<Set<BilanPremiosVo>> ListeBilanPremiosPourAnnee(@PathVariable String date) {
        try {
            Set<BilanPremiosVo> bilans = bilanPremiosFacade.listerBilanPremiosPourAnnee(date);
            if (bilans.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(bilans, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
