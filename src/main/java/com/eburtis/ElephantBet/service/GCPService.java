package com.eburtis.ElephantBet.service;

import com.eburtis.ElephantBet.domain.ticket.Ticket;
import com.eburtis.ElephantBet.facade.TicketFacade;
import com.eburtis.ElephantBet.presentation.vo.ResponseEnregistrementGCP;
import com.eburtis.ElephantBet.service.servicehelper.CsvServiceHelper;
import com.google.api.gax.paging.Page;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class GCPService {
    private final CsvServiceHelper ticketServiceHelper;
    @Value("${gcp.config.file}")
    private String gcpConfigFile;
    @Value("${gcp.project.id}")
    private String gcpProjectId;
    @Value("${gcp.bucket.id}")
    private String gcpBucketId;
    public GCPService(CsvServiceHelper ticketServiceHelper) {
        this.ticketServiceHelper = ticketServiceHelper;

    }

    @Transactional
    public ResponseEnregistrementGCP telechargerFichierEtConvertirCsv() throws IOException {
        try{
            // chargement de clé API
            InputStream inputStream = new ClassPathResource(gcpConfigFile).getInputStream();
            // configuration de connection
            StorageOptions options = StorageOptions.newBuilder().setProjectId(gcpProjectId)
                    .setCredentials(GoogleCredentials.fromStream(inputStream)).build();
            // connexion aux services google cloud
            Storage storage = options.getService();
            // charger tous les fichiers
            Page<Blob> listFichier = storage.list(gcpBucketId,Storage.BlobListOption.prefix("export_last_day_tickets"));
            // lecteur du fichier
            if(listFichier != null){
                Blob fichier = listFichier.getValues().iterator().next();
                if(fichier.getContentType().equalsIgnoreCase("plain/text")){
                    InputStream file = new ByteArrayInputStream(fichier.getContent());
                    // lecture des lignes du fichier csv
                    List<Ticket> tickets = this.ticketServiceHelper.convertirCsvToTicket(file);
                    return new ResponseEnregistrementGCP(tickets,fichier);
                }
            }
            else {
                throw new RuntimeException("Ficher non trouvé");
            }
        }
        catch(Exception e){
            throw new RuntimeException("Erreur lors de la sauvegarde des données du fichier csv." + e.getMessage());
        }
        return null;
    }


}
