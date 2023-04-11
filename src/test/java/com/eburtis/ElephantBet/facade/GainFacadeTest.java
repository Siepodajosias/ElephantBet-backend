package com.eburtis.ElephantBet.facade;

import com.eburtis.ElephantBet.domain.Parametrage;
import com.eburtis.ElephantBet.domain.Parametrages;
import com.eburtis.ElephantBet.domain.ticket.Ticket;
import com.eburtis.ElephantBet.repository.bilanGain.BilanGainRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GainFacadeTest {

    @InjectMocks
    private GainFacade gainFacade;
    @Mock
    private BilanGainRepository bilanGainRepository;
    @Mock
    private ParametrageRepository parametrageRepository;

    @BeforeEach
    void setUp() {
        gainFacade=new GainFacade(bilanGainRepository, bilanGainRepository, parametrageRepository);
    }

    @Test
    void should_enregistrer_gain() throws Exception {
        // given
        Gain gain = new Gain();
        List<Gain> gains = new ArrayList<>();
        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(new Ticket("AZZKEKAKZKZ", LocalDate.now(), 200,0,
        "NULL", "123222","Abobo", "1234","Abobo","pending", "NULL"));
        given(bilanGainRepository.saveAll(any(Set.class))).willReturn(gains);
        // when
        gainFacade.enregistrerGain(ticketList);
        //then
        verify(bilanGainRepository).saveAll(any(Set.class));
    }

    @Test
    void should_liste_bilanGains_pour_periode() {
        // given
        String debut = "2022-02-02";
        String fin = "2022-02-03";
        Integer limite = 2000;
        List<Parametrage> parametrageList = new ArrayList<>();
        parametrageList.add(new Parametrage(Parametrages.LIMITES_GAINS,0,0,"23444",LocalDate.parse("2022-10-18"),LocalDate.parse("2022-10-20")));
        List<Object[]> list = new ArrayList<>();
        // when
        lenient().when(parametrageRepository.listeParametragePourPeriode(LocalDate.parse(debut),
                LocalDate.parse(fin), Parametrages.LIMITES_GAINS)).thenReturn(parametrageList);
        lenient().when(bilanGainRepository.listBilanGainPourPeriode(LocalDate.parse(debut),
                LocalDate.parse(fin),parametrageList)).thenReturn(list);
        lenient().when(bilanGainRepository.listBilanGainAvecPrixSuperieurALimitePourPeriode(LocalDate.parse(debut),
                LocalDate.parse(fin),limite,parametrageList)).thenReturn(list);
        lenient().when(bilanGainRepository.listBilanGainAvecPrixInferieurALimitePourPeriode(LocalDate.parse(debut),
                LocalDate.parse(fin),limite,parametrageList)).thenReturn(list);
        gainFacade.listeBilanGainsPourPeriode(debut,fin,limite);
        // then
        verify(parametrageRepository).listeParametragePourPeriode(LocalDate.parse(debut),
                LocalDate.parse(fin), Parametrages.LIMITES_GAINS);

        verify(bilanGainRepository).listBilanGainPourPeriode(LocalDate.parse(debut),
                LocalDate.parse(fin),parametrageList);

//        verify(bilanGainRepository).listBilanGainAvecPrixSuperieurALimitePourPeriode(LocalDate.parse(debut),
//                LocalDate.parse(fin),limite,parametrageList);

//        verify(bilanGainRepository).listBilanGainAvecPrixInferieurALimitePourPeriode(LocalDate.parse(debut),
//                LocalDate.parse(fin),limite,parametrageList);
//
    }

    @Test
    void should_liste_bilanGains_pour_mois() {
        // given
        String date = "2022-02";
        Integer annee = 2022;
        Integer mois = 2;
        Integer limite = 2000;
        List<Parametrage> parametrageList = new ArrayList<>();
        parametrageList.add(new Parametrage(Parametrages.LIMITES_GAINS,0,0,"23444",LocalDate.parse("2022-10-18"),LocalDate.parse("2022-10-20")));
        List<Object[]> list = new ArrayList<>();
        // when
        lenient().when(parametrageRepository.listeParametragePourMois(mois,annee, Parametrages.LIMITES_GAINS)).thenReturn(parametrageList);

        lenient().when(bilanGainRepository.listBilanGainPourMois(annee,mois,parametrageList)).thenReturn(list);

        lenient().when(bilanGainRepository.listBilanGainAvecPrixSuperieurALimitePourMois(annee,mois,limite,parametrageList)).thenReturn(list);

        lenient().when(bilanGainRepository.listBilanGainAvecPrixInferieurALimitePourMois(annee,mois,limite,parametrageList)).thenReturn(list);

        gainFacade.listerBilanGainsPourMois(date,limite);
        // then
        verify(parametrageRepository).listeParametragePourMois(mois,annee, Parametrages.LIMITES_GAINS);

        verify(bilanGainRepository).listBilanGainPourMois(annee,mois,parametrageList);

//        verify(bilanGainRepository).listBilanGainAvecPrixSuperieurALimitePourMois(annee,mois,limite,parametrageList);

//        verify(bilanGainRepository).listBilanGainAvecPrixInferieurALimitePourMois(annee,mois,limite,parametrageList);
//
    }


    @Test
    void should_liste_bilanGains_pour_annee() {
        // given
        String date = "2022";
        Integer annee = 2022;
        Integer limite = 2000;
        List<Parametrage> parametrageList = new ArrayList<>();
        parametrageList.add(new Parametrage(Parametrages.LIMITES_GAINS,0,0,"23444",LocalDate.parse("2022-10-18"),LocalDate.parse("2022-10-20")));
        List<Object[]> list = new ArrayList<>();
        // when
        lenient().when(parametrageRepository.listeParametragePourAnnee(annee, Parametrages.LIMITES_GAINS)).thenReturn(parametrageList);

        lenient().when(bilanGainRepository.listBilanGainPourAnnee(annee,parametrageList)).thenReturn(list);

        lenient().when(bilanGainRepository.listBilanGainAvecPrixSuperieurALimitePourAnnee(annee,limite,parametrageList)).thenReturn(list);

        lenient().when(bilanGainRepository.listBilanGainAvecPrixInferieurALimitePourAnnee(annee,limite,parametrageList)).thenReturn(list);

        gainFacade.listeBilanGainsPourAnnee(date,limite);
        // then
        verify(parametrageRepository).listeParametragePourAnnee(annee, Parametrages.LIMITES_GAINS);

        verify(bilanGainRepository).listBilanGainPourAnnee(annee,parametrageList);

//        verify(bilanGainRepository).listBilanGainAvecPrixSuperieurALimitePourAnnee(annee,limite,parametrageList);

//        verify(bilanGainRepository).listBilanGainAvecPrixInferieurALimitePourAnnee(annee,limite,parametrageList);
//
    }


}