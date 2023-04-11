package com.eburtis.ElephantBet.repository.ticket;

import com.eburtis.ElephantBet.domain.Parametrage;
import com.eburtis.ElephantBet.domain.ticket.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>, TicketRepositoryCustom {

    @Query(value = "SELECT DISTINCT staff_creator_group_id FROM ticket WHERE staff_creator_group_id LIKE %?1% LIMIT 10", nativeQuery = true)
    Set<String> listeDesGroupesId(@Param("groupeId") String groupeId);

    @Query("SELECT ticket FROM Ticket ticket WHERE (ticket.dateOfCreation BETWEEN :dateDebut AND :dateFin)")
    List<Ticket> listeTicketPourPeriode(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);
}

interface TicketRepositoryCustom {
    // Récuperation des données d'une année
    List<Object[]> listTicketPourAnnee(Integer annee, List<Parametrage> parametrages);
    List<Object[]> listTicketAvecPrixSuperieurALimitePourAnnee(Integer annee, Integer limite, List<Parametrage> parametrages);
    List<Object[]> listTicketAvecPrixInferieurALimitePourAnnee(Integer annee, Integer limite, List<Parametrage> parametrages);

    // Récuperation des données d'un mois
    List<Object[]> listTicketPourMois(Integer annee, Integer mois, List<Parametrage> parametrages);
    List<Object[]> listTicketAvecPrixSuperieurALimitePourMois(Integer annee, Integer mois, Integer limite, List<Parametrage> parametrages);
    List<Object[]> listTicketAvecPrixInferieurALimitePourMois(Integer annee, Integer mois, Integer limite, List<Parametrage> parametrages);

    // Récuperation des données d'une période
    List<Object[]> listTicketPourPeriode(LocalDate dateDebut, LocalDate dateFin, List<Parametrage> parametrages);
    List<Object[]> listTicketAvecPrixSuperieurALimitePourPeriode(LocalDate dateDebut, LocalDate dateFin, Integer limite, List<Parametrage> parametrages);
    List<Object[]> listTicketAvecPrixInferieurALimitePourPeriode(LocalDate dateDebut, LocalDate dateFin, Integer limite, List<Parametrage> parametrages);
}
