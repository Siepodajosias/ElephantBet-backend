package com.eburtis.ElephantBet.facade;

import com.eburtis.ElephantBet.domain.*;
import com.eburtis.ElephantBet.domain.ticket.Ticket;
import com.eburtis.ElephantBet.presentation.vo.BilanMisesVo;
import com.eburtis.ElephantBet.repository.bilanMise.BilanMiseRepository;
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


import static com.eburtis.ElephantBet.domain.Parametrages.LIMITE_TICKETS;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Service
public class BilanMiseFacade {
    private final TicketRepository ticketRepository;
    private final ParametrageBilanRepository parametrageBilanRepository;
    private final ParametrageRepository parametrageRepository;
    private final BilanMiseRepository bilanMiseRepository;

    public BilanMiseFacade(TicketRepository ticketRepository, ParametrageBilanRepository parametrageBilanRepository, ParametrageRepository parametrageRepository, BilanMiseRepository bilanMiseRepository) {
        this.ticketRepository = ticketRepository;
        this.parametrageBilanRepository = parametrageBilanRepository;
        this.parametrageRepository = parametrageRepository;
        this.bilanMiseRepository = bilanMiseRepository;
    }


    /**
     * Cette fonction stock tous les tickets de mise superieures ou egal 50 000 kz dans la table Bilan mise
     */
    @Transactional
    public void enregistrerBilanMise(LocalDate dateDebut, LocalDate dateFin) {
        //  TODO: Delete en fonction de la date
        EParametrageBilan eParametrageBilan = EParametrageBilan.valueOf("MISES");
        ParametrageBilan parametrageBilan = this.parametrageBilanRepository.findByNom(eParametrageBilan);
        this.bilanMiseRepository.supprimerBilanMisePourPeriode(dateDebut, dateFin);
        this.bilanMiseRepository.flush();
        List<Ticket> ticketList = this.ticketRepository.listeTicketPourPeriode(dateDebut,dateFin).stream()
                .filter(element -> element.getTicketPrice() > parametrageBilan.getLimite())
                .collect(toList());
        Set<BilanMise> listeBilanMise = ticketList.stream()
                .map(BilanMise::new).collect(toSet());
        bilanMiseRepository.saveAll(listeBilanMise);
    }

    /**
     * Lister les bilans de mise pour une période passée en paramètre
     * @param debut  la date début de la période
     * @param fin    la date de fin de la période
     * @param limite la limite
     * @return liste de bilan mise pour une période.
     */
    @Transactional(readOnly = true)
    public Set<BilanMisesVo> listeBilanMisePourPeriode(String debut, String fin, Integer limite) {
        LocalDate dateDebut = LocalDate.parse(debut);
        LocalDate dateFin = LocalDate.parse(fin);
        // liste des exclusions pour cette période
        List<Parametrage> parametrages = this.parametrageRepository.listeParametragePourPeriode(dateDebut, dateFin, LIMITE_TICKETS);
        Set<BilanMisesVo> bilans = new HashSet<>();
        // liste des bilans de mise pour cette période
        List<Object[]> bilansMisesTotalExistants = this.bilanMiseRepository.listTicketPourPeriode(dateDebut, dateFin, parametrages);
        if (bilansMisesTotalExistants.isEmpty()){
            return emptySet();
        }
        // liste des bilans de mises supérieure à la limite
        List<Object[]> bilansExistantsSuperieurALimite = this.bilanMiseRepository.listTicketAvecPrixSuperieurALimitePourPeriode(dateDebut, dateFin, limite, parametrages);
        return mettreAJourValeursDesMises(bilans, bilansMisesTotalExistants, bilansExistantsSuperieurALimite);
    }

    /**
     *  Cette fonction permet de mettre à jour les valeurs des bilans de mises
     * @param bilans
     * @param bilansMisesTotalExistants
     * @param bilansExistantsSuperieurALimite
     * @return
     */
    @Transactional(readOnly = true)
    private Set<BilanMisesVo> mettreAJourValeursDesMises(Set<BilanMisesVo> bilans, List<Object[]> bilansMisesTotalExistants,
                                                         List<Object[]> bilansExistantsSuperieurALimite) {
        // Mettre à jour toutes les valeurs
        bilansMisesTotalExistants.forEach(bilan -> {
            String groupeName = (String) bilan[0];
            BilanMisesVo bilanMisesVo = new BilanMisesVo(groupeName, 0L, 0L);
            bilans.add(bilanMisesVo);
        });
        // Mettre à jour les valeurs supérieures à la limite
        bilansExistantsSuperieurALimite.forEach(bilan -> {
            BigInteger nombreTicketSuperieur = (BigInteger) bilan[1];
            BigInteger totalMiseSuperieur = (BigInteger) bilan[2];
            bilans.forEach(bil -> {
                if (bil.getStaffCreatorGroupName().equalsIgnoreCase(bilan[0].toString())){
                    bil.mettreAJourTicketValeurSuperieure(nombreTicketSuperieur.longValue(), totalMiseSuperieur.longValue());
                }
            });
        });
        return bilans;
    }

    /**
     * Lister bilan mise pour un mois passé en paramètre.
     * @param date   le mois concerné
     * @param limite la limite à appliquer
     * @return Une liste de bilan mise pour un mois.
     */
    @Transactional(readOnly = true)
    @Cacheable("mises-mensuel")
    public Set<BilanMisesVo> listerBilanGainPourMois(String date, Integer limite) {
        Integer annee = Integer.valueOf(date.split("-")[0]);
        Integer mois = Integer.valueOf(date.split("-")[1]);
        // liste des exclusions de cette période
        List<Parametrage> parametrages = this.parametrageRepository.listeParametragePourMois(mois, annee, LIMITE_TICKETS);
        Set<BilanMisesVo> bilans = new HashSet<>();
        // liste des bilans de mises sur cette période
        List<Object[]> bilansMisesTotalExistants = this.bilanMiseRepository.listTicketPourMois(annee, mois, parametrages);
        if (bilansMisesTotalExistants.isEmpty()){
            return emptySet();
        };
        // liste des bilans de mises supérieure à la limite
        List<Object[]> bilansExistantsSuperieurALimite = this.bilanMiseRepository.listTicketAvecPrixSuperieurALimitePourMois(annee, mois, limite, parametrages);
        return mettreAJourValeursDesMises(bilans, bilansMisesTotalExistants, bilansExistantsSuperieurALimite);
    }

    /**
     * Lister les bilans de mise pour une année.
     * @param date   l'année concernée
     * @param limite la limite
     * @return Une liste de bilan mise pour une année.
     */
    @Transactional(readOnly = true)
    @Cacheable("mises-annuelle")
    public Set<BilanMisesVo> listerBilanMisesPourAnnee(String date, Integer limite) {
        Integer annee = Integer.valueOf(date);
        // liste des exclusions de cette période
        List<Parametrage> parametrages = this.parametrageRepository.listeParametragePourAnnee(annee, LIMITE_TICKETS);
        Set<BilanMisesVo> bilans = new HashSet<>();
        // liste des bilans de mises pour cette période
        List<Object[]> bilansMisesTotalExistants = this.bilanMiseRepository.listTicketPourAnnee(annee, parametrages);
        if (bilansMisesTotalExistants.isEmpty()){
            return emptySet();
        }
        // liste des bilans de mises supérieure à la limite
        List<Object[]> bilansExistantsSuperieurALimite = this.bilanMiseRepository.listTicketAvecPrixSuperieurALimitePourAnnee(annee, limite, parametrages);
        return mettreAJourValeursDesMises(bilans, bilansMisesTotalExistants, bilansExistantsSuperieurALimite);
    }



}
