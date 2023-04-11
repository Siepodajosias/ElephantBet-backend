package com.eburtis.ElephantBet.repository.parametrage;

import com.eburtis.ElephantBet.domain.Parametrage;
import com.eburtis.ElephantBet.domain.Parametrages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParametrageRepository extends JpaRepository<Parametrage, Long> {
    Optional<List<Parametrage>> findByCode(Parametrages code);

    @Query("SELECT parametrage FROM Parametrage parametrage WHERE (parametrage.dateDebutLimite BETWEEN :dateDebut AND :dateFin) AND (parametrage.dateFinLimite BETWEEN :dateDebut AND :dateFin) AND parametrage.code=:code")
    List<Parametrage> listeParametragePourPeriode(@Param("dateDebut") LocalDate dateDebut, @Param("dateFin") LocalDate dateFin, @Param("code") Parametrages code);

    @Query("SELECT parametrage FROM Parametrage parametrage WHERE (year(parametrage.dateDebutLimite)=:annee  and month(parametrage.dateDebutLimite)=:mois) and parametrage.code=:code")
    List<Parametrage> listeParametragePourMois(@Param("mois") Integer mois, @Param("annee") Integer annee, @Param("code") Parametrages code);

    @Query("SELECT parametrage FROM Parametrage parametrage WHERE year(parametrage.dateDebutLimite)=:annee and parametrage.code=:code")
    List<Parametrage> listeParametragePourAnnee(@Param("annee") Integer annee, @Param("code") Parametrages code);
}

