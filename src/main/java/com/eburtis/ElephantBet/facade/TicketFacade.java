package com.eburtis.ElephantBet.facade;

import com.eburtis.ElephantBet.domain.Fichier;
import com.eburtis.ElephantBet.domain.ticket.Ticket;
import com.eburtis.ElephantBet.presentation.vo.ResponseEnregistrementGCP;
import com.eburtis.ElephantBet.repository.bilanMise.BilanMiseRepository;
import com.eburtis.ElephantBet.repository.FichierRepository;
import com.eburtis.ElephantBet.repository.parametrage.ParametrageRepository;
import com.eburtis.ElephantBet.repository.ticket.TicketRepository;
import com.eburtis.ElephantBet.service.FTPService;
import com.eburtis.ElephantBet.service.MailService;
import com.eburtis.ElephantBet.service.GCPService;
import com.eburtis.ElephantBet.service.servicehelper.CsvServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.eburtis.ElephantBet.domain.ticket.StatutTicket.WIN;
import static java.lang.String.valueOf;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
public class TicketFacade {

    private final int NOMBRE_JOUR_PERIODE = 4;
    private final TicketRepository ticketRepository;
    private final GCPService gcpService;
    private final ParametrageRepository parametrageRepository;
    private final BilanMiseRepository bilanMiseRepository;
    private final BilanGeneralFacade bilanGeneralFacade;

    private final BilanGainFacade bilanGainFacade;
    private final BilanMiseFacade bilanMiseFacade;
    private final CsvServiceHelper ticketServiceHelper;
    private final FichierRepository fichierRepository;
    private final MailService mailService;

    @Autowired
    private final CacheManager cacheManager;


    private final FTPService ftpService;

    /**
     * Constructeur de la facade Ticket
     *
     * @param ticketRepository
     * @param gcpService
     * @param parametrageRepository
     * @param bilanMiseRepository
     * @param bilanGeneralFacade
     * @param bilanGainFacade
     * @param bilanMiseFacade
     * @param ticketServiceHelper
     * @param fichierRepository
     * @param mailService
     * @param cacheManager
     * @param ftpService
     */
    public TicketFacade(TicketRepository ticketRepository, GCPService gcpService, ParametrageRepository parametrageRepository,
                        BilanMiseRepository bilanMiseRepository, BilanGeneralFacade bilanGeneralFacade,
                        BilanGainFacade bilanGainFacade, BilanMiseFacade bilanMiseFacade,
                        CsvServiceHelper ticketServiceHelper, FichierRepository fichierRepository,
                        MailService mailService, CacheManager cacheManager, FTPService ftpService) {
        this.ticketRepository = ticketRepository;
        this.gcpService = gcpService;
        this.parametrageRepository = parametrageRepository;
        this.bilanMiseRepository = bilanMiseRepository;
        this.bilanGeneralFacade = bilanGeneralFacade;
        this.bilanGainFacade = bilanGainFacade;
        this.bilanMiseFacade = bilanMiseFacade;
        this.ticketServiceHelper = ticketServiceHelper;
        this.fichierRepository = fichierRepository;
        this.mailService = mailService;
        this.cacheManager = cacheManager;
        this.ftpService = ftpService;
    }

    /**
     * Importer le fichier csv des tickets par un formulaire
     * @param file le fichier des données
     */
    @Transactional
    public void importerParFormulaire(MultipartFile file) throws IOException {
        List<Ticket> ticketsConvertis = ticketServiceHelper.convertirCsvToTicket(file.getInputStream());

        // Determination de la date la plus recente et la plus ancienne
        long count = ticketsConvertis.stream().count();
        LocalDate dateDebut = ticketsConvertis.stream().sorted(Comparator.comparing(Ticket::getDateOfCreation)).findFirst().get().getDateOfCreation();
        LocalDate dateFin = ticketsConvertis.stream().sorted(Comparator.comparing(Ticket::getDateOfCreation)).skip(count - 1).findFirst().get().getDateOfCreation();

        // Determination de la liste des dates par periode d'insertions
        List<LocalDate> periodes = this.listeDesPeriodesEnregistrement(dateDebut, dateFin);
        periodes.forEach(periode -> {
            List<Ticket> listTicketFiltre = new ArrayList<>();
            LocalDate debut = periode;
            LocalDate fin = periode.plusDays(NOMBRE_JOUR_PERIODE);
            List<Ticket> listTicketFiltreEntre = ticketsConvertis.stream()
                    .filter(element -> (element.getDateOfCreation().isAfter(debut)) && (element.getDateOfCreation().isBefore(fin))).collect(toList());
            List<Ticket> listTicketFiltreEgale = ticketsConvertis.stream()
                    .filter(element -> (element.getDateOfCreation().equals(debut)) || (element.getDateOfCreation().equals(fin))).collect(toList());
            listTicketFiltre.addAll(listTicketFiltreEntre);
            listTicketFiltre.addAll(listTicketFiltreEgale);
            // enregistrement des bilans
            this.enregistrerTicket(listTicketFiltre, debut, fin);
        });
        this.bilanMiseFacade.enregistrerBilanMise(dateDebut,dateFin);
        this.bilanGainFacade.enregistrerBilanGain(dateDebut,dateFin);
        cacheManager.getCacheNames().parallelStream().forEach(name -> cacheManager.getCache(name).clear());
    }

    /**
     * Cette fonction permet de télécharger les fichiers sur GCP et enregistrer les bilans
     * @throws Exception
     */
    @Transactional
    public void telechargerFichierSurGCP() throws Exception {
        // lecteur et transformation du csv en list de tickets
        ResponseEnregistrementGCP fichierConverti = gcpService.telechargerFichierEtConvertirCsv();

        // recuperation de la date la plus recente et la plus ancienne
        List<Ticket> ticketsConvertis = fichierConverti.getTickets();
        List<Ticket> listTicketOrdonne= ticketsConvertis.stream().sorted(Comparator.comparing(Ticket::getDateOfCreation)).collect(toList());
        LocalDate dateDebut = listTicketOrdonne.stream().findFirst().get().getDateOfCreation();
        long count = ticketsConvertis.stream().count();
        LocalDate dateFin = listTicketOrdonne.stream().skip(count - 1).findFirst().get().getDateOfCreation();
        // Decoupage des tickets en periode d'enregistrement
        List<LocalDate> periodes = this.listeDesPeriodesEnregistrement(dateDebut,dateFin);
        //enregistrement des tickets par periodes
        periodes.forEach(periode->{
            this.enregistrementDesBilans(ticketsConvertis,dateDebut,dateFin,periode,periode.plusDays(NOMBRE_JOUR_PERIODE));
        });
        //Enregistrement des bilans
        this.bilanMiseFacade.enregistrerBilanMise(dateDebut,dateFin);
        this.bilanGainFacade.enregistrerBilanGain(dateDebut,dateFin);

        // enregistrement des infos du fichier
        Fichier newFichier = new Fichier(fichierConverti.getFichier().getName(),fichierConverti.getTickets().size(),
                fichierConverti.getFichier().getSize(), LocalDateTime.now());
        this.fichierRepository.save(newFichier);
        // Envoi des emails
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd à HH:mm");
        Date dateActuelle = new Date();
        String strDate = sdf.format(dateActuelle);
        String text = "<h2>Validation d'enregistrement dans la base de données.</h2>" +
                        "<h3>Le fichier a bien été enregistré le "+ strDate+".</h3><br>"
                        + "<h3>Cordialement</h3>"
                        + "<h3>EBURTIS</h3> <br>";
        this.mailService.envoyerMailDeValidation(text);
        cacheManager.getCacheNames().parallelStream().forEach(name -> cacheManager.getCache(name).clear());

    }


    @Scheduled(cron = "0 0 4 * * *")
    public void telechargerFichierDuJour() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd a HH:mm:ss.SSS");
        Date debut = new Date();
        String strDateDebut = sdf.format(debut);
        System.out.println("Debut de telechargement: " + strDateDebut);
        telechargerFichierSurGCP();
        Date fin = new Date();
        String strDateFin = sdf.format(fin);
        System.out.println("Fin de telechargement: " + strDateFin);

    }

    /**
     * Fonction permettant de copier le fichier sur le serveur FTP
     * @throws Exception
     */
    @Scheduled(cron = "0 0 5 * * *")
    public void enregistrementFichierSurServeurFtp() throws Exception {
        ResponseEnregistrementGCP reponseGCP = gcpService.telechargerFichierEtConvertirCsv();
        //enregistrement du fichier sur le serveur ftp
        ftpService.EnregistrerFichierSurFTP(reponseGCP.getFichier().getContent());
        // Envoi des emails
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd à HH:mm");
        Date dateActuelle = new Date();
        String strDate = sdf.format(dateActuelle);
        String text = "<h2>Validation d'enregistrement sur le serveur FTP.</h2>" +
                "<h3>Le fichier a bien été enregistré sur serveur FTP le "+ strDate+".</h3><br>"
                + "<h3>Cordialement</h3>"
                + "<h3>EBURTIS</h3> <br>";
        this.mailService.envoyerMailDeValidation(text);
        cacheManager.getCacheNames().parallelStream().forEach(name -> cacheManager.getCache(name).clear());
    }


    /**
     * Cette fonction permet de creer une liste de periode avec un decallage de 4 jours
     * @param dateDebut date de debut des periodes
     * @param dateFin date de fin des periodes
     * @return liste des dates
     */
    public List<LocalDate> listeDesPeriodesEnregistrement(LocalDate dateDebut, LocalDate dateFin) {
        List<LocalDate> listeDate = new ArrayList<>();
        LocalDate debut = dateDebut;
        while (debut.isBefore(dateFin) || debut.isEqual(dateFin)) {
            listeDate.add(debut);
            debut = debut.plusDays(10);
        }
        if(!listeDate.contains(dateFin)){
            listeDate.add(dateFin);
        }
        return listeDate.stream().sorted().collect(Collectors.toList());
    }

    /**
     * Cette fonction permet d'enregistrer les differents bilans
     * @param ticketList liste des tickets a enregistrer
     * @param dateDebut date la plus recente
     * @param dateFin date la plus ancienne
     * @param debut date de debut d'enregistrement
     * @param fin date de fin d'enregistrement
     */
    @Transactional
    public void enregistrementDesBilans(List<Ticket> ticketList, LocalDate dateDebut,LocalDate dateFin,LocalDate debut,LocalDate fin){
        // liste des tickets de la periode
        List<Ticket> listTicketEnregistrement = new ArrayList<>();
        // liste des tickets dans l'intervalle
        List<Ticket> listTicketDansPeriode = ticketList.stream()
                .filter(element -> (element.getDateOfCreation().isAfter(debut)) && (element.getDateOfCreation().isBefore(fin))).collect(toList());
        // liste des tickets aux bornes de l'intervalle
        List<Ticket> listTicketAuxBornes = ticketList.stream()
                .filter(element -> (element.getDateOfCreation().equals(debut)) || (element.getDateOfCreation().equals(fin))).collect(toList());
        // combinaison des listes
        listTicketEnregistrement.addAll(listTicketAuxBornes);
        listTicketEnregistrement.addAll(listTicketDansPeriode);
        // Enregistrement des bilans
        this.enregistrerTicket(listTicketEnregistrement,debut,fin);
        bilanGeneralFacade.enregistrerBilanGeneral(listTicketEnregistrement,debut,fin);

    }

    /**
     * Cette fonction permet d'enregistrer les tickets
     * @param tickets
     * @throws Exception
     */
    @Transactional
    public void enregistrerTicket(List<Ticket> tickets,LocalDate dateDebut ,LocalDate datefin ){
        Map<String, Ticket> ticketsParNumero = this.ticketRepository.listeTicketPourPeriode(dateDebut,datefin).stream()
                .collect(toMap(Ticket::getTicketReference, identity()));

        Set<Ticket> listeEnregistrementTicket = new HashSet<>();
        // Enregistrement des tickets
        tickets.forEach(ticket->{
            Ticket existanTicket = ticketsParNumero.get(ticket.getTicketReference());
            if(existanTicket != null) {
                if (existanTicket.getTicketStatus().equalsIgnoreCase(valueOf(WIN))){
                    existanTicket.mettreStatusGagnant(ticket.getTicketStatus(), ticket.getAmtWon(), ticket.getDateWhenWon());
                }
                else{
                    existanTicket.mettreStatusAjour(ticket.getTicketStatus());
                }
            }
            else{
                listeEnregistrementTicket.add(ticket);
            }
        });
        this.ticketRepository.saveAll(listeEnregistrementTicket);
    }


    /**
     * Cette fonction permet de liste le groupeId en fonction de ce qui l'utilisateur à saisir
     * @param groupeId
     * @return
     */
    @Transactional
    public Set<String> listeDesGroupesId(String groupeId) {
        return ticketRepository.listeDesGroupesId(groupeId);
    }
}
