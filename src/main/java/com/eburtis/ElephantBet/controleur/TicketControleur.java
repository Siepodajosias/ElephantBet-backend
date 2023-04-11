package com.eburtis.ElephantBet.controleur;

import com.eburtis.ElephantBet.facade.TicketFacade;
import com.eburtis.ElephantBet.presentation.vo.MessageReponse;
import com.eburtis.ElephantBet.service.servicehelper.CsvServiceHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@RestController
@RequestMapping("/api/ticket")
public class TicketControleur {

    private final TicketFacade ticketFacade;
    private final CsvServiceHelper ticketServiceHelper;

    public TicketControleur(TicketFacade ticketFacade, CsvServiceHelper ticketServiceHelper) {
        this.ticketFacade = ticketFacade;
        this.ticketServiceHelper = ticketServiceHelper;
    }

    @PostMapping("/upload")
    public ResponseEntity<MessageReponse> importer(@RequestParam("file") MultipartFile file) {
        String message = "";
        if (ticketServiceHelper.estFormatCSV(file)) {
            try {
                ticketFacade.importerParFormulaire(file);
                message = "Import du fichier reussi: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new MessageReponse(message));
            } catch (Exception e) {
                message = "Import du fichier en echec: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new MessageReponse(message));
            }
        }
        message = "Mauvais format du fichier";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageReponse(message));
    }

    @GetMapping("/groupeId/{groupeId}")
    public ResponseEntity<Set<String>> listeDesGroupesId(@PathVariable String groupeId) {
        try {
            Set<String> villes = ticketFacade.listeDesGroupesId(groupeId);
            if (villes.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(villes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
