package com.eburtis.ElephantBet.repository.bilanGeneral;

import com.eburtis.ElephantBet.domain.BilanGeneral;
import com.eburtis.ElephantBet.domain.Parametrage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BilanGeneralRepository extends JpaRepository<BilanGeneral,Long>,BilanGeneralRepositoryCustom {

    @Query("SELECT bilan FROM BilanGeneral bilan WHERE (bilan.dateCreation BETWEEN :dateDebut AND :dateFin)")
    List<BilanGeneral> listeBilanSeptsDerniersJours(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);

}
interface BilanGeneralRepositoryCustom {
    //RECUPERATION DES DONNEES POUR L'ANNEE.//
    List<Object[]> listBilanPourAnnee(Integer annee, List<Parametrage> parametrages);


    //RECUPERATION DES DONNEES POUR LE MOIS.//
    List<Object[]> listBilanPourMois(Integer annee, Integer mois, List<Parametrage> parametrages);


    //RECUPERATION DES DONNEES POUR LE JOURNEE.//
    List<Object[]> listBilanPourPeriode(LocalDate dateDebut, LocalDate dateFin, List<Parametrage> parametrages);

}

