package com.eburtis.ElephantBet.repository.bilanGain;

import com.eburtis.ElephantBet.domain.BilanGain;
import com.eburtis.ElephantBet.domain.Parametrage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BilanGainRepository extends JpaRepository<BilanGain, Long>,BilanGainRepositoryCustom {

    @Modifying
    @Query("DELETE FROM BilanGain bilan_gain WHERE (bilan_gain.dateOfCreation BETWEEN :dateDebut AND :dateFin)")
    void supprimerBilanGainPourPeriode(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);
}

interface BilanGainRepositoryCustom {
    //RECUPERATION DES DONNEES POUR L'ANNEE.//
    List<Object[]> listBilanGainPourAnnee(Integer annee, List<Parametrage> parametrages);

    List<Object[]> listBilanGainAvecPrixSuperieurALimitePourAnnee(Integer annee, Integer limite, List<Parametrage> parametrages);

    List<Object[]> listBilanGainAvecPrixInferieurALimitePourAnnee(Integer annee, Integer limite, List<Parametrage> parametrages);

    //RECUPERATION DES DONNEES POUR LE MOIS.//
    List<Object[]> listBilanGainPourMois(Integer annee, Integer mois, List<Parametrage> parametrages);

    List<Object[]> listBilanGainAvecPrixSuperieurALimitePourMois(Integer annee, Integer mois, Integer limite, List<Parametrage> parametrages);

    List<Object[]> listBilanGainAvecPrixInferieurALimitePourMois(Integer annee, Integer mois, Integer limite, List<Parametrage> parametrages);

    //RECUPERATION DES DONNEES POUR LE JOURNEE.//
    List<Object[]> listBilanGainPourPeriode(LocalDate dateDebut, LocalDate dateFin, List<Parametrage> parametrages);

    List<Object[]> listBilanGainAvecPrixSuperieurALimitePourPeriode(LocalDate dateDebut, LocalDate dateFin, Integer limite, List<Parametrage> parametrages);

    List<Object[]> listBilanGainAvecPrixInferieurALimitePourPeriode(LocalDate dateDebut, LocalDate dateFin, Integer limite, List<Parametrage> parametrages);
}

