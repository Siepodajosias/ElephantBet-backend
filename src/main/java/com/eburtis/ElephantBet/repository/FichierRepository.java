package com.eburtis.ElephantBet.repository;

import com.eburtis.ElephantBet.domain.Fichier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FichierRepository  extends JpaRepository<Fichier,Long> {
}
