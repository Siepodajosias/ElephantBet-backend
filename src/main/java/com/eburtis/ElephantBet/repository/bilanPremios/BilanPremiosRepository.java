package com.eburtis.ElephantBet.repository.bilanPremios;

import com.eburtis.ElephantBet.domain.BilanJeu;
import com.eburtis.ElephantBet.domain.BilanPremios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BilanPremiosRepository extends JpaRepository<BilanPremios,Long> {

    @Query("SELECT bilan_premios FROM BilanPremios bilan_premios WHERE (bilan_premios.dateEvenement BETWEEN :dateDebut AND :dateFin)")
    List<BilanPremios> listeBilanPremiosPourPeriode(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);

    @Query(value = "select agence, sum(case when valeur is not null then valeur else 0 end) as valeur_total, count(case when agence is not null then 1 else 0 end) as numero " +
            "from bilan_premios where date_evenement between :dateDebut and :dateFin group by agence", nativeQuery = true)
    List<Object[]> listeBilanJeuPourPeriodeCalculer(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin);

    @Query(value = "select agence, sum(case when valeur is not null then valeur else 0 end) as valeur_total, count(case when agence is not null then 1 else 0 end) as numero " +
            "from bilan_premios where date_part('year', date_evenement) =:annee  and date_part('month', date_evenement)=:mois group by agence", nativeQuery = true)
    List<Object[]> listeBilanPremiosPourMoisCalculer(@Param("mois") Integer mois, @Param("annee") Integer annee);

    @Query(value = "select agence, sum(case when valeur is not null then valeur else 0 end) as valeur_total, count(case when agence is not null then 1 else 0 end) as numero " +
            "from bilan_premios where date_part('year', date_evenement)=:annee group by agence", nativeQuery = true)
    List<Object[]> listeBilanPremiosPourAnneeCalculer(@Param("annee") Integer annee);
}
