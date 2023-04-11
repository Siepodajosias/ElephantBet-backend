package com.eburtis.ElephantBet.facade;

import com.eburtis.ElephantBet.domain.BilanGeneral;
import com.eburtis.ElephantBet.domain.Parametrage;
import com.eburtis.ElephantBet.domain.Parametrages;
import com.eburtis.ElephantBet.domain.ticket.Ticket;
import com.eburtis.ElephantBet.repository.bilanGeneral.BilanRepository;
import com.eburtis.ElephantBet.repository.parametrage.ParametrageRepository;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BilanFacadeTest {
    @InjectMocks
    private BilanGeneralFacade bilanFacade;
    @Mock
    private BilanRepository bilanRepository;
    @Mock
    private ParametrageRepository parametrageRepository;

    @BeforeEach
    void setUp() {
//        bilanFacade = new BilanFacade(bilanRepository, ticketRepository, parametrageRepository);
    }


    @Test
    public void should_enregistrer_bilan() {
        // given
        BilanGeneral bilan = new BilanGeneral();
        List<BilanGeneral> bilans = new ArrayList<>();
        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(new Ticket("AZZKEKAKZKZ", LocalDate.now(), 200,0,
                "NULL", "123222","Abobo", "1234",
                "Abobo","pending", "NULL"));

        given(bilanRepository.saveAll(any(Set.class))).willReturn(bilans);
        // when
        bilanFacade.enregistrerBilan(ticketList);
        //then
        verify(bilanRepository).saveAll(any(Set.class));
    }


    @Test
    void should_lister_bilan_pour_periode() {
        // given
        String debut = "2022-02-02";
        String fin = "2022-02-03";
        List<Parametrage> parametrageList = new ArrayList<>();
        parametrageList.add(new Parametrage(Parametrages.GROUPES_ET_DATES,0,0,"23444",LocalDate.parse("2022-10-18"),LocalDate.parse("2022-10-20")));
        List<Object[]> list = new ArrayList<>();
        // when
        lenient().when(parametrageRepository.listeParametragePourPeriode(LocalDate.parse(debut),
                LocalDate.parse(fin), Parametrages.GROUPES_ET_DATES)).thenReturn(parametrageList);
        lenient().when(bilanRepository.listBilanPourPeriode(LocalDate.parse(debut),
                LocalDate.parse(fin),parametrageList)).thenReturn(list);
        bilanFacade.listerBilanPourPeriode(debut,fin);
        //then
        verify(parametrageRepository).listeParametragePourPeriode(LocalDate.parse(debut),
                LocalDate.parse(fin), Parametrages.GROUPES_ET_DATES);
        verify(bilanRepository).listBilanPourPeriode(LocalDate.parse(debut),
                LocalDate.parse(fin),parametrageList);
    }

    @Test
    void should_liste_bilan_par_mois() {
        // given
        String date = "2022-02";
        Integer annee = 2022;
        Integer mois = 2;
        List<Parametrage> parametrageList = new ArrayList<>();
        parametrageList.add(new Parametrage(Parametrages.GROUPES_ET_DATES,0,0,"23444",LocalDate.parse("2022-10-18"),LocalDate.parse("2022-10-20")));
        List<Object[]> list = new ArrayList<>();
        // when
        lenient().when(parametrageRepository.listeParametragePourMois(mois,annee, Parametrages.GROUPES_ET_DATES)).thenReturn(parametrageList);
        lenient().when(bilanRepository.listBilanPourMois(annee,mois,parametrageList)).thenReturn(list);

        bilanFacade.listeBilanParMois(date);
        //then
        verify(parametrageRepository).listeParametragePourMois(mois,annee, Parametrages.GROUPES_ET_DATES);
        verify(bilanRepository).listBilanPourMois(annee,mois,parametrageList);
    }

    @Test
    void should_liste_bilan_journalier_par_annee() {
        // given
        String date = "2022";
        Integer annee = 2022;
        List<Parametrage> parametrageList = new ArrayList<>();
        parametrageList.add(new Parametrage(Parametrages.GROUPES_ET_DATES,0,0,"23444",LocalDate.parse("2022-10-18"),LocalDate.parse("2022-10-20")));
        List<Object[]> list = new ArrayList<>();
        // when
        lenient().when(parametrageRepository.listeParametragePourAnnee(annee, Parametrages.GROUPES_ET_DATES)).thenReturn(parametrageList);
        lenient().when(bilanRepository.listBilanPourAnnee(annee,parametrageList)).thenReturn(list);

        bilanFacade.listeBilanJournalierParAnnee(date);
        //then
        verify(parametrageRepository).listeParametragePourAnnee(annee, Parametrages.GROUPES_ET_DATES);
        verify(bilanRepository).listBilanPourAnnee(annee,parametrageList);

    }
}