package com.eburtis.ElephantBet.facade;

import com.eburtis.ElephantBet.domain.BilanGeneral;
import com.eburtis.ElephantBet.domain.Parametrage;
import com.eburtis.ElephantBet.domain.ticket.Ticket;
import com.eburtis.ElephantBet.presentation.vo.BilanGeneralVo;
import com.eburtis.ElephantBet.repository.bilanGeneral.BilanGeneralRepository;
import com.eburtis.ElephantBet.repository.parametrage.ParametrageRepository;
import com.eburtis.ElephantBet.repository.ticket.TicketRepository;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.eburtis.ElephantBet.domain.Parametrages.GROUPES_ET_DATES;
import static com.eburtis.ElephantBet.domain.ticket.StatutTicket.*;
import static java.lang.String.valueOf;
import static java.util.Collections.emptySet;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

@Service
public class BilanGeneralFacade {
    private final BilanGeneralRepository bilanGeneralRepository;

    private final TicketRepository ticketRepository;
    private final ParametrageRepository parametrageRepository;

    /**
     * Constructor de la facade de Bilan
     *
     * @param bilanRepository       repository du bilan
     * @param ticketRepository
     * @param parametrageRepository repository du paramétrage
     */
    public BilanGeneralFacade(BilanGeneralRepository bilanRepository, TicketRepository ticketRepository, ParametrageRepository parametrageRepository) {
        this.bilanGeneralRepository = bilanRepository;
        this.ticketRepository = ticketRepository;
        this.parametrageRepository = parametrageRepository;
    }

    /**
     * Cette fonction est chargée de faire les calcul et stockage un ensemble de bilan
     */
    @Transactional
    public void enregistrerBilanGeneral(List<Ticket> ticketList,LocalDate dateDebut,LocalDate datefin) {
        Set<BilanGeneral> bilans = this.calculBilansGeneral(ticketList,dateDebut,datefin);
        this.bilanGeneralRepository.saveAll(bilans);
    }

    /**
     * Cette fonction calcul les bilans à partir d'une liste de ticket
     * @return le bilan sur tous les tickets.
     */
    @Transactional
    public Set<BilanGeneral> calculBilansGeneral(List<Ticket> ticketList, LocalDate dateDebut, LocalDate datefin) {
        Set<BilanGeneral> bilans = new HashSet<>();
        // Regroupe des bilans par (jour, (nom du groupe et id du groupe)).
        Map<Pair<LocalDate, Pair<String, String>>, BilanGeneral> bilanExistantParJourneeEtVille = bilanGeneralRepository.listeBilanSeptsDerniersJours(dateDebut,datefin).stream()
                .collect(toMap(entry -> Pair.of(entry.getDateCreation(), Pair.of(entry.getStaffCreatorGroupId(), entry.getStaffCreatorGroupName())), identity()));

        // Regroupe des anciens tickets par (jour, (nom du groupe et id du groupe)).
        Map<Pair<LocalDate, Pair<String, String>>, List<Ticket>> ticketParDateDeCreationEtVille = ticketRepository.listeTicketPourPeriode(dateDebut,datefin).stream()
                .collect(groupingBy(entry -> Pair.of(entry.getDateOfCreation(), Pair.of(entry.getStaffCreatorGroupId(), entry.getStaffCreatorGroupName()))));

        // Enregistrment des nouveaux tickets
        for (Map.Entry<Pair<LocalDate, Pair<String, String>>, List<Ticket>> entry : ticketParDateDeCreationEtVille.entrySet()) {
            Pair<LocalDate, Pair<String, String>> key = entry.getKey();
            Pair<String, String> ville = key.getRight();
            List<Ticket> ticketPourUneVille = entry.getValue();

            // Calcul du nombre de tickets gagnant
            long nombreTicketGagnant = ticketPourUneVille.stream()
                    .filter(element -> element.getTicketStatus().equalsIgnoreCase(valueOf(WIN)))
                    .count();

            //  Calcul du nombre de tickets perdant
            long nombreTicketLost = ticketPourUneVille.stream()
                    .filter(element -> element.getTicketStatus().equalsIgnoreCase(valueOf(LOST)))
                    .count();

            // Calcul du nombre de ticket en pending
            long nombreTicketPending = ticketPourUneVille.stream()
                    .filter(element -> element.getTicketStatus().equalsIgnoreCase(valueOf(PENDING)))
                    .count();

            // Total des tickets.
            long nombreTickets = ticketPourUneVille.size();

            // Calcul de la valeur des tickets
            long valeurTickets = ticketPourUneVille.stream()
                    .map(Ticket::getTicketPrice)
                    .reduce(0, Integer::sum);

            // Calcul du total des gains
            long totalGains = ticketPourUneVille.stream()
                    .filter(element -> element.getTicketStatus().equalsIgnoreCase(valueOf(WIN)))
                    .map(Ticket::getAmtWon)
                    .reduce(0, Integer::sum);

            BilanGeneral bilan = bilanExistantParJourneeEtVille.get(Pair.of(key.getLeft(), key.getRight()));
            if(bilan != null){
                bilan.mettreAJourBilanAvecNouveau( nombreTickets, nombreTicketGagnant, nombreTicketPending,nombreTicketLost,
                        totalGains, valeurTickets);
            }
            else{
                bilan = new BilanGeneral(key.getLeft(), ville.getRight(), ville.getLeft(),
                        nombreTickets, nombreTicketGagnant, nombreTicketPending,nombreTicketLost,
                        totalGains, valeurTickets);
                bilans.add(bilan);
            }
            // Ajout du bilan
        }
        return bilans;
    }

    /**
     * Lister les bilans pour une période
     * @param debut la date de début.
     * @param fin   la date de fin.
     * @return Une liste de bilan.
     */
    @Transactional
    public Set<BilanGeneralVo> listerBilanGeneralPourPeriode(String debut, String fin) {
        LocalDate dateDebut = LocalDate.parse(debut);
        LocalDate dateFin = LocalDate.parse(fin);
        // liste des exclusions dans cette période
        List<Parametrage> parametrages = this.parametrageRepository.listeParametragePourPeriode(dateDebut, dateFin, GROUPES_ET_DATES);
        Set<BilanGeneralVo> bilans = new HashSet<>();
        // liste des bilans de cette période
        List<Object[]> bilansExistant= this.bilanGeneralRepository.listBilanPourPeriode(dateDebut,dateFin,parametrages);
        if (bilansExistant.isEmpty()) {
            return emptySet();
        }
        else {
            // création des bilans
            bilansExistant.forEach(bilan->{
                String groupeName= (String) bilan[0];
                BigDecimal nombreTicketVendu = (BigDecimal) bilan[1];
                BigDecimal totalMise = (BigDecimal) bilan[2];
                BigDecimal nombreTicketGagnant = (BigDecimal) bilan[3];
                BigDecimal totalGains = (BigDecimal) bilan[4];
                BigDecimal nombreTicketPending = (BigDecimal) bilan[5];
                long balance= totalMise.longValue() - totalGains.longValue();
                BilanGeneralVo bilanGeneralVo = new BilanGeneralVo(groupeName,nombreTicketVendu.longValue(),nombreTicketGagnant.longValue(),
                        nombreTicketPending.longValue(),totalGains.longValue(),totalMise.longValue(),balance);
                bilans.add(bilanGeneralVo);
            });
            return bilans;
        }
    }

    /**
     * Lister les bilans pour un mois passé en paramètre.
     * @param date le mois concerné
     * @return La liste de bilan pour le mois.
     */
    @Transactional
    @Cacheable("bilans-mensuel")
    public Set<BilanGeneralVo> listeBilanGeneralPourMois(String date) {
        Integer annee = Integer.valueOf(date.split("-")[0]);
        Integer mois = Integer.valueOf(date.split("-")[1]);
        Set<BilanGeneralVo> bilans = new HashSet<>();
        // liste des exclusions de cette période
        List<Parametrage> parametrages = this.parametrageRepository.listeParametragePourMois(mois, annee, GROUPES_ET_DATES);
        // liste des bilans de cette période
        List<Object[]> bilansExistant= this.bilanGeneralRepository.listBilanPourMois(annee,mois,parametrages);
        if (bilansExistant.isEmpty()) {
            return emptySet();
        }
        else {
            // création des bilans
            bilansExistant.forEach(bilan->{
                String groupeName= (String) bilan[0];
                BigDecimal nombreTicketVendu = (BigDecimal) bilan[1];
                BigDecimal totalMise = (BigDecimal) bilan[2];
                BigDecimal nombreTicketGagnant = (BigDecimal) bilan[3];
                BigDecimal totalGains = (BigDecimal) bilan[4];
                BigDecimal nombreTicketPending = (BigDecimal) bilan[5];
                long balance= totalMise.longValue() - totalGains.longValue();
                BilanGeneralVo bilanGeneralVo = new BilanGeneralVo(groupeName,nombreTicketVendu.longValue(),nombreTicketGagnant.longValue(),
                        nombreTicketPending.longValue(),totalGains.longValue(),totalMise.longValue(),balance);
                bilans.add(bilanGeneralVo);
            });
            return bilans;
        }
    }

    /**
     * Lister les bilans pour une année passée en paramètre.
     * @param date l'année.
     * @return les bilans de cette période
     */
    @Transactional
    @Cacheable("bilan-annuel")
    public Set<BilanGeneralVo> listeBilanGeneralPourAnnee(String date) {
        Integer annee = Integer.valueOf(date);
        // liste des exlusions de cette période
        List<Parametrage> parametrages = this.parametrageRepository.listeParametragePourAnnee(annee, GROUPES_ET_DATES);
        // liste des bilans de cette période
        Set<BilanGeneralVo> bilans = new HashSet<>();
        List<Object[]> bilansExistant= this.bilanGeneralRepository.listBilanPourAnnee(annee,parametrages);
        if (bilansExistant.isEmpty()) {
            return emptySet();
        }
        else {
            //création des bilans
            bilansExistant.forEach(bilan->{
                String groupeName= (String) bilan[0];
                BigDecimal nombreTicketVendu = (BigDecimal) bilan[1];
                BigDecimal totalMise = (BigDecimal) bilan[2];
                BigDecimal nombreTicketGagnant = (BigDecimal) bilan[3];
                BigDecimal totalGains = (BigDecimal) bilan[4];
                BigDecimal nombreTicketPending = (BigDecimal) bilan[5];
                long balance= totalMise.longValue() - totalGains.longValue();
                BilanGeneralVo bilanGeneralVo = new BilanGeneralVo(groupeName,nombreTicketVendu.longValue(),nombreTicketGagnant.longValue()
                                                                   ,nombreTicketPending.longValue(),totalGains.longValue(),totalMise.longValue(),balance);
                bilans.add(bilanGeneralVo);
            });
            return bilans;
        }
    }
}
