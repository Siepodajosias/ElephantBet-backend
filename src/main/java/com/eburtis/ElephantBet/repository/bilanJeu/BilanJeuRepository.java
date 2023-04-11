package com.eburtis.ElephantBet.repository.bilanJeu;

import com.eburtis.ElephantBet.domain.BilanJeu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BilanJeuRepository extends JpaRepository<BilanJeu,Long> {

    @Query("SELECT bilan_jeu FROM BilanJeu bilan_jeu WHERE (bilan_jeu.dateCreation BETWEEN :dateDebut AND :dateFin)")
    List<BilanJeu> listeBilanJeuPourPeriode(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);

    @Query(value = "select nom_jeu, sum(case when nombre_jeu is not null then nombre_jeu else 0 end) as nombre_jeu, " +
            "sum(case when volume_jeu is not null then volume_jeu else 0 end) as volume_jeu, " +
            "sum(case when nombre_gains is not null then nombre_gains else 0 end) as nombre_gains," +
            "sum(case when volume_gains is not null then volume_gains else 0 end) as volume_gains, " +
            "sum(case when balance is not null then balance else 0 end) as balance, " +
            "sum(case when nombre_gains_superieur_limite is not null then nombre_gains_superieur_limite else 0 end) as nombre_gains_superieur_limite, " +
            "sum(case when nombre_gains_inferieur_limite is not null then nombre_gains_inferieur_limite else 0 end) as nombre_gains_inferieur_limite, " +
            "sum(case when volume_gains_superieur_limite is not null then volume_gains_superieur_limite else 0 end) as volume_gains_superieur_limite, " +
            "sum(case when volume_gains_inferieur_limite is not null then volume_gains_inferieur_limite else 0 end) as volume_gains_inferieur_limite " +
            "from bilan_jeu where date_creation between :dateDebut and :dateFin group by nom_jeu", nativeQuery = true)
    List<Object[]> listeBilanJeuPourPeriodeCalculer(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);

    @Query(value = "select nom_jeu, sum(case when nombre_jeu is not null then nombre_jeu else 0 end) as nombre_jeu, " +
            "sum(case when volume_jeu is not null then volume_jeu else 0 end) as volume_jeu, " +
            "sum(case when nombre_gains is not null then nombre_gains else 0 end) as nombre_gains," +
            "sum(case when volume_gains is not null then volume_gains else 0 end) as volume_gains, " +
            "sum(case when balance is not null then balance else 0 end) as balance, " +
            "sum(case when nombre_gains_superieur_limite is not null then nombre_gains_superieur_limite else 0 end) as nombre_gains_superieur_limite, " +
            "sum(case when nombre_gains_inferieur_limite is not null then nombre_gains_inferieur_limite else 0 end) as nombre_gains_inferieur_limite, " +
            "sum(case when volume_gains_superieur_limite is not null then volume_gains_superieur_limite else 0 end) as volume_gains_superieur_limite, " +
            "sum(case when volume_gains_inferieur_limite is not null then volume_gains_inferieur_limite else 0 end) as volume_gains_inferieur_limite " +
            "from bilan_jeu where date_part('year', date_creation) =:annee  and date_part('month', date_creation)=:mois group by nom_jeu", nativeQuery = true)
    List<Object[]> listeBilanJeuPourMoisCalculer(@Param("mois") Integer mois, @Param("annee") Integer annee);

    @Query(value = "select nom_jeu, sum(case when nombre_jeu is not null then nombre_jeu else 0 end) as nombre_jeu, " +
            "sum(case when volume_jeu is not null then volume_jeu else 0 end) as volume_jeu, " +
            "sum(case when nombre_gains is not null then nombre_gains else 0 end) as nombre_gains," +
            "sum(case when volume_gains is not null then volume_gains else 0 end) as volume_gains, " +
            "sum(case when balance is not null then balance else 0 end) as balance, " +
            "sum(case when nombre_gains_superieur_limite is not null then nombre_gains_superieur_limite else 0 end) as nombre_gains_superieur_limite, " +
            "sum(case when nombre_gains_inferieur_limite is not null then nombre_gains_inferieur_limite else 0 end) as nombre_gains_inferieur_limite, " +
            "sum(case when volume_gains_superieur_limite is not null then volume_gains_superieur_limite else 0 end) as volume_gains_superieur_limite, " +
            "sum(case when volume_gains_inferieur_limite is not null then volume_gains_inferieur_limite else 0 end) as volume_gains_inferieur_limite " +
            "from bilan_jeu where date_part('year', date_creation)=:annee group by nom_jeu", nativeQuery = true)
    List<Object[]> listeBilanJeuPourAnneeCalculer(@Param("annee") Integer annee);


}
