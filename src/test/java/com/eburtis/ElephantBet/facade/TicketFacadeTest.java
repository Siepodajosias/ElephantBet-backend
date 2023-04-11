package com.eburtis.ElephantBet.facade;

import com.eburtis.ElephantBet.domain.Parametrage;
import com.eburtis.ElephantBet.domain.Parametrages;
import com.eburtis.ElephantBet.domain.ticket.Ticket;
import com.eburtis.ElephantBet.repository.FichierRepository;
import com.eburtis.ElephantBet.repository.parametrage.ParametrageRepository;
import com.eburtis.ElephantBet.repository.ticket.TicketRepository;
import com.eburtis.ElephantBet.service.FTPService;
import com.eburtis.ElephantBet.service.MailService;
import com.eburtis.ElephantBet.service.GCPService;
import com.eburtis.ElephantBet.service.servicehelper.CsvServiceHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class TicketFacadeTest {

    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private GCPService telechargerFichierGCSService;
    @Mock
    private ParametrageRepository parametrageRepository;
    @Mock
    private BilanGeneralFacade bilanFacade;
    @Mock
    private GainFacade gainFacade;

    @Mock
    private FTPService ftpService;

    @Mock
    private MailService mailService;

    @Mock
    private FichierRepository fichierRepository;
    @Mock
    private CsvServiceHelper ticketServiceHelper;
    @InjectMocks
    private TicketFacade ticketFacade;

    @BeforeEach
    void setUp() {
        ticketFacade = new TicketFacade(ticketRepository,telechargerFichierGCSService,parametrageRepository, bilanMiseRepository, bilanFacade,gainFacade, bilanGainFacade, bilanMiseFacade, ticketServiceHelper, fichierRepository, mailService, cacheManager, ftpService);
    }



    @Test
    public void should_enregistrer_ticket() throws Exception {
        // given
        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(new Ticket("AZZKEKAKZKZ", LocalDate.now(), 200,0,
                "NULL", "123222","Abobo", "1234",
                "Abobo","pending", "NULL"));
        // when
        when(ticketRepository.saveAll(any(Set.class))).thenReturn(ticketList);
        when(ticketRepository.findAll()).thenReturn(ticketList);
       // ticketFacade.enregistrerTicket(ticketList);
        // then
        verify(ticketRepository).saveAll(any(Set.class));
        verify(ticketRepository).findAll();
    }

    @Test
    public void should_liste_bilan_mise_pour_periode() {
        // given
        String debut = "2022-02-02";
        String fin = "2022-02-03";
        Integer limite = 2000;
        List<Parametrage> parametrageList = new ArrayList<>();
        parametrageList.add(new Parametrage(Parametrages.LIMITE_TICKETS,0,0,"23444",LocalDate.parse("2022-10-18"),LocalDate.parse("2022-10-20")));
        List<Object[]> list = new ArrayList<>();
        // when
        lenient().when(parametrageRepository.listeParametragePourPeriode(LocalDate.parse(debut),
                LocalDate.parse(fin), Parametrages.LIMITE_TICKETS)).thenReturn(parametrageList);
        lenient().when(ticketRepository.listTicketPourPeriode(LocalDate.parse(debut),
                LocalDate.parse(fin),parametrageList)).thenReturn(list);
        lenient().when(ticketRepository.listTicketAvecPrixSuperieurALimitePourPeriode(LocalDate.parse(debut),
                LocalDate.parse(fin),limite,parametrageList)).thenReturn(list);
        lenient().when(ticketRepository.listTicketAvecPrixInferieurALimitePourPeriode(LocalDate.parse(debut),
                LocalDate.parse(fin),limite,parametrageList)).thenReturn(list);
        ticketFacade.listeBilanMisePourPeriode(debut,fin,limite);
        // then
        verify(parametrageRepository).listeParametragePourPeriode(LocalDate.parse(debut),
                LocalDate.parse(fin), Parametrages.LIMITE_TICKETS);

        verify(ticketRepository).listTicketPourPeriode(LocalDate.parse(debut),
                LocalDate.parse(fin),parametrageList);
    }

    @Test
    public void should_lister_bilan_gain_pour_mois() {
        // given
        String date = "2022-02";
        Integer annee = 2022;
        Integer mois = 2;
        Integer limite = 2000;
        List<Parametrage> parametrageList = new ArrayList<>();
        parametrageList.add(new Parametrage(Parametrages.LIMITE_TICKETS,0,0,"23444",LocalDate.parse("2022-10-18"),LocalDate.parse("2022-10-20")));
        List<Object[]> list = new ArrayList<>();
        // when
        lenient().when(parametrageRepository.listeParametragePourMois(mois,annee, Parametrages.LIMITE_TICKETS)).thenReturn(parametrageList);

        lenient().when(ticketRepository.listTicketPourMois(annee,mois,parametrageList)).thenReturn(list);

        lenient().when(ticketRepository.listTicketAvecPrixSuperieurALimitePourMois(annee,mois,limite,parametrageList)).thenReturn(list);

        lenient().when(ticketRepository.listTicketAvecPrixInferieurALimitePourMois(annee,mois,limite,parametrageList)).thenReturn(list);

        ticketFacade.listerBilanGainPourMois(date,limite);
        // then
        verify(parametrageRepository).listeParametragePourMois(mois,annee, Parametrages.LIMITE_TICKETS);

        verify(ticketRepository).listTicketPourMois(annee,mois,parametrageList);

    }

    @Test
    public void sholud_lister_bilan_mises_pour_annee() {
        // given
        String date = "2022";
        Integer annee = 2022;
        Integer limite = 2000;
        List<Parametrage> parametrageList = new ArrayList<>();
        parametrageList.add(new Parametrage(Parametrages.LIMITE_TICKETS,0,0,"23444",LocalDate.parse("2022-10-18"),LocalDate.parse("2022-10-20")));
        List<Object[]> list = new ArrayList<>();
        // when
        lenient().when(parametrageRepository.listeParametragePourAnnee(annee, Parametrages.LIMITE_TICKETS)).thenReturn(parametrageList);

        lenient().when(ticketRepository.listTicketPourAnnee(annee,parametrageList)).thenReturn(list);

        lenient().when(ticketRepository.listTicketAvecPrixSuperieurALimitePourAnnee(annee,limite,parametrageList)).thenReturn(list);

        lenient().when(ticketRepository.listTicketAvecPrixInferieurALimitePourAnnee(annee,limite,parametrageList)).thenReturn(list);

        ticketFacade.listerBilanMisesPourAnnee(date,limite);
        // then
        verify(parametrageRepository).listeParametragePourAnnee(annee, Parametrages.LIMITE_TICKETS);

        verify(ticketRepository).listTicketPourAnnee(annee,parametrageList);
    }

    @Test
    void listeDesGroupesId() {
    }
}