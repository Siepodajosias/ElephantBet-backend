package com.eburtis.ElephantBet.facade;

import com.eburtis.ElephantBet.domain.BilanGain;
import com.eburtis.ElephantBet.domain.EParametrageBilan;
import com.eburtis.ElephantBet.domain.Parametrage;
import com.eburtis.ElephantBet.domain.ParametrageBilan;
import com.eburtis.ElephantBet.domain.ticket.Ticket;
import com.eburtis.ElephantBet.presentation.vo.BilanGainsVo;
import com.eburtis.ElephantBet.repository.bilanGain.BilanGainRepository;

import com.eburtis.ElephantBet.repository.parametrage.ParametrageBilanRepository;
import com.eburtis.ElephantBet.repository.parametrage.ParametrageRepository;
import com.eburtis.ElephantBet.repository.ticket.TicketRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.eburtis.ElephantBet.domain.Parametrages.LIMITES_GAINS;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Service
public class BilanGainFacade {

    private final TicketRepository ticketRepository;
    private final ParametrageBilanRepository parametrageBilanRepository;

    private final ParametrageRepository parametrageRepository;
    private final BilanGainRepository bilanGainRepository;

    public BilanGainFacade(TicketRepository ticketRepository, ParametrageBilanRepository parametrageBilanRepository, ParametrageRepository parametrageRepository, BilanGainRepository bilanGainRepository) {
        this.ticketRepository = ticketRepository;
        this.parametrageBilanRepository = parametrageBilanRepository;
        this.parametrageRepository = parametrageRepository;
        this.bilanGainRepository = bilanGainRepository;
    }

    /**
     * Cette fonction stock tous les tickets de mise superieures ou egal 250 000 kz dans la table Bilan mise
     */
    @Transactional
    public void enregistrerBilanGain(LocalDate dateDebut, LocalDate dateFIn) {
        EParametrageBilan eParametrageBilan = EParametrageBilan.valueOf("GAINS");
        ParametrageBilan parametrageBilan = this.parametrageBilanRepository.findByNom(eParametrageBilan);
        this.bilanGainRepository.supprimerBilanGainPourPeriode(dateDebut, dateFIn);
        this.bilanGainRepository.flush();
        List<Ticket> ticketList = this.ticketRepository.listeTicketPourPeriode(dateDebut,dateFIn).stream()
                .filter(element -> element.getAmtWon() > parametrageBilan.getLimite() && element.getTicketStatus().equalsIgnoreCase("win") )
                .collect(toList());
        Set<BilanGain> listeBilanGain = ticketList.stream().map(BilanGain::new).collect(toSet());
        bilanGainRepository.saveAll(listeBilanGain);
    }

    /**
     * Lister les bilans de mise pour une période passée en paramètre
     * @param debut  la date début de la période
     * @param fin    la date de fin de la période
     * @param limite la limite
     * @return liste de bilan mise pour une période.
     */
    @Transactional(readOnly = true)
    public Set<BilanGainsVo> listeBilanGainsPourPeriode(String debut, String fin, Integer limite) {
        LocalDate dateDebut = LocalDate.parse(debut);
        LocalDate dateFin = LocalDate.parse(fin);
        // liste des exclusions de cette période
        List<Parametrage> parametrages = this.parametrageRepository.listeParametragePourPeriode(dateDebut, dateFin, LIMITES_GAINS);
        Set<BilanGainsVo> bilans = new HashSet<>();
        // liste des gains après les exclusions
        List<Object[]> bilansMisesTotalExistants = this.bilanGainRepository.listBilanGainPourPeriode(dateDebut, dateFin, parametrages);
        if (bilansMisesTotalExistants.isEmpty()) {
            return emptySet();
        }
        // calcul des gains supérieurs à la limite
        List<Object[]> bilansExistantsSuperieurALimite = this.bilanGainRepository.listBilanGainAvecPrixSuperieurALimitePourPeriode(dateDebut, dateFin, limite, parametrages);
        // calcul des gains inférieurs à la limite

        return mettreAJourValeursDesGains(bilans, bilansMisesTotalExistants, bilansExistantsSuperieurALimite);
    }

    /**
     * cette fonction permet de mettre à jour les valeurs des gains
     * @param bilans
     * @param bilansGainsTotalExistants
     * @param bilansExistantsSuperieurALimite
     * @return
     */
    @Transactional(readOnly = true)
    private Set<BilanGainsVo> mettreAJourValeursDesGains(Set<BilanGainsVo> bilans, List<Object[]> bilansGainsTotalExistants, List<Object[]> bilansExistantsSuperieurALimite) {
        // Mettre à jour toutes les valeurs
        bilansGainsTotalExistants.forEach(bilan -> {
            String groupeName = (String) bilan[0];
            BilanGainsVo bilanGainsVo = new BilanGainsVo(groupeName, 0L, 0L);
            bilans.add(bilanGainsVo);
        });
        // Mettre à jour les valeurs supérieures à la limite
        bilansExistantsSuperieurALimite.forEach(bilan -> {
            BigInteger nombreTicketSuperieur = (BigInteger) bilan[1];
            BigInteger totalMiseSuperieur = (BigInteger) bilan[2];
            bilans.forEach(bil -> {
                if (bil.getStaffCreatorGroupName().equalsIgnoreCase(bilan[0].toString())) {
                    bil.mettreAJourTicketValeurSuperieure(nombreTicketSuperieur.longValue(), totalMiseSuperieur.longValue());
                }
            });
        });

        return bilans;
    }

    /**
     * Lister les bilans de gains pour un mois passé en paramètre.
     *
     * @param date   le mois concerné
     * @param limite la limite.
     * @return Une liste de bilan gains pour un mois
     */
    @Transactional(readOnly = true)
    @Cacheable("gains-mensuel")
    public Set<BilanGainsVo> listerBilanGainsPourMois(String date, Integer limite) {
        Integer annee = Integer.valueOf(date.split("-")[0]);
        Integer mois = Integer.valueOf(date.split("-")[1]);
        // liste des exclusions dans cette période
        List<Parametrage> parametrages = this.parametrageRepository.listeParametragePourMois(mois, annee, LIMITES_GAINS);
        Set<BilanGainsVo> bilans = new HashSet<>();
        // liste des gains de cette période
        List<Object[]> bilansMisesTotalExistants = this.bilanGainRepository.listBilanGainPourMois(annee, mois, parametrages);
        if (bilansMisesTotalExistants.isEmpty()) {
            return emptySet();
        }
        // calcul des gains supérieurs à la limite
        List<Object[]> bilansExistantsSuperieurALimite = this.bilanGainRepository.listBilanGainAvecPrixSuperieurALimitePourMois(annee, mois, limite, parametrages);
        return mettreAJourValeursDesGains(bilans, bilansMisesTotalExistants, bilansExistantsSuperieurALimite);
    }

    /**
     * Lister des bilans de gains pour une année passé en paramètre.
     *
     * @param date   l'année
     * @param limite La limite à appliquer
     * @return Une liste de bilan Gains.
     */
    @Transactional(readOnly = true)
    @Cacheable("gains-annuel")
    public Set<BilanGainsVo> listeBilanGainsPourAnnee(String date, Integer limite) {
        Integer annee = Integer.valueOf(date);
        // liste des exclusions de cette période
        List<Parametrage> parametrages = this.parametrageRepository.listeParametragePourAnnee(annee, LIMITES_GAINS);
        Set<BilanGainsVo> bilans = new HashSet<>();
        // liste des gains de cette période
        List<Object[]> bilansMisesTotalExistants = this.bilanGainRepository.listBilanGainPourAnnee(annee, parametrages);
        if (bilansMisesTotalExistants.isEmpty()) {
            return emptySet();
        }
        // calcul des gains supérieurs à la limite
        List<Object[]> bilansExistantsSuperieurALimite = this.bilanGainRepository.listBilanGainAvecPrixSuperieurALimitePourAnnee(annee, limite, parametrages);
        // calcul des gains inférieurs à la limite
        return mettreAJourValeursDesGains(bilans, bilansMisesTotalExistants, bilansExistantsSuperieurALimite);
    }
}
