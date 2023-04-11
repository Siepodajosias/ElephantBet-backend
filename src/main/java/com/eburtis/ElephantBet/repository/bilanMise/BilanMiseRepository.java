package com.eburtis.ElephantBet.repository.bilanMise;

import com.eburtis.ElephantBet.domain.BilanMise;
import com.eburtis.ElephantBet.domain.Parametrage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BilanMiseRepository extends JpaRepository<BilanMise,Long>, BilanMiseRepositoryCustom{

    @Modifying
    @Query("DELETE FROM BilanMise bilan_mise WHERE (bilan_mise.dateOfCreation BETWEEN :dateDebut AND :dateFin)")
    void supprimerBilanMisePourPeriode(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);
}


interface BilanMiseRepositoryCustom {
    //RECUPERATION DES DONNEES POUR L'ANNEE.//
    List<Object[]> listTicketPourAnnee(Integer annee, List<Parametrage> parametrages);

    List<Object[]> listTicketAvecPrixSuperieurALimitePourAnnee(Integer annee, Integer limite, List<Parametrage> parametrages);

    List<Object[]> listTicketAvecPrixInferieurALimitePourAnnee(Integer annee, Integer limite, List<Parametrage> parametrages);

    //RECUPERATION DES DONNEES POUR LE MOIS.//
    List<Object[]> listTicketPourMois(Integer annee, Integer mois, List<Parametrage> parametrages);

    List<Object[]> listTicketAvecPrixSuperieurALimitePourMois(Integer annee, Integer mois, Integer limite, List<Parametrage> parametrages);

    List<Object[]> listTicketAvecPrixInferieurALimitePourMois(Integer annee, Integer mois, Integer limite, List<Parametrage> parametrages);

    //RECUPERATION DES DONNEES POUR LE JOURNEE.//
    List<Object[]> listTicketPourPeriode(LocalDate dateDebut, LocalDate dateFin, List<Parametrage> parametrages);

    List<Object[]> listTicketAvecPrixSuperieurALimitePourPeriode(LocalDate dateDebut, LocalDate dateFin, Integer limite, List<Parametrage> parametrages);

    List<Object[]> listTicketAvecPrixInferieurALimitePourPeriode(LocalDate dateDebut, LocalDate dateFin, Integer limite, List<Parametrage> parametrages);
}

