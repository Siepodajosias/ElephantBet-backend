package com.eburtis.ElephantBet.service.servicehelper;

import com.eburtis.ElephantBet.domain.ticket.Ticket;
import com.eburtis.ElephantBet.repository.ticket.TicketRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.*;

@Service
public class CsvServiceHelper {

    private final TicketRepository ticketRepository;

    public CsvServiceHelper(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public static String TYPE = "text/csv";
    static String[] HEADERs = {"ticketReference",
            "dateOfCreation", "ticketPrice",
            "amtWon", "dateWhenWon", "staffCreatorId", "staffCreator_name", "staffCreator_groupId",
            "staffCreator_groupName", "ticketStatus", "staffPay_group"};


    /**
     * Verifie si le fichier importer est de type csv.
     *
     * @param file le fichier
     * @return true si c'est un csv, false sinon.
     */
    public boolean estFormatCSV(MultipartFile file) {
        return TYPE.equals(file.getContentType());
    }

    /**
     * Transformer les données du fichier csv en objet Ticket.
     *
     * @param fichier le fichier en entrée.
     * @return
     */
    public List<Ticket> convertirCsvToTicket(InputStream fichier) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(fichier, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
            List<Ticket> tickets = new ArrayList<>();
            Iterable<CSVRecord> lignesCsv = csvParser.getRecords();
            // parcours des lignes du fichier csv
            for (CSVRecord ligneCsv : lignesCsv) {
                // collecte des donnees
                String ticketReference = ligneCsv.get("ticketReference");
                LocalDate dateOfCreation = LocalDate.parse(ligneCsv.get("dateOfCreation").split("T")[0]);
                int ticketPrice = Integer.parseInt(ligneCsv.get("ticketPrice"));
                int amtWon = Integer.parseInt(ligneCsv.get("amtWon"));
                String dateWhenWon = ligneCsv.get("dateWhenWon");
                if (!ligneCsv.get("dateWhenWon").equalsIgnoreCase("NULL")) {
                    dateWhenWon = ligneCsv.get("dateWhenWon").split("T")[0];
                }
                String staffCreatorId = ligneCsv.get("staffCreatorId");
                String staffCreatorName = ligneCsv.get("staffCreator_name");
                String staffCreatorGroupId = ligneCsv.get("staffCreator_groupAgencyId");
                String staffCreatorGroupName = ligneCsv.get("staffCreator_groupAgencyName");
                String ticketStatus = ligneCsv.get("ticketStatus");
                String staffPayGroup = ligneCsv.get("staffPay_agencyName");
                Ticket ticket = new Ticket(ticketReference, dateOfCreation, ticketPrice, amtWon, dateWhenWon,
                                           staffCreatorId, staffCreatorName, staffCreatorGroupId, staffCreatorGroupName,
                                           ticketStatus, staffPayGroup);
                tickets.add(ticket);
            }

            return tickets;
        } catch (IOException e) {
            throw new RuntimeException("Une erreur lors la conversion du fichier: " + e.getMessage());
        }
    }

}

