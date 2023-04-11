package com.eburtis.ElephantBet.repository.parametrage;

import com.eburtis.ElephantBet.domain.EParametrageBilan;
import com.eburtis.ElephantBet.domain.Parametrage;
import com.eburtis.ElephantBet.domain.ParametrageBilan;
import com.eburtis.ElephantBet.domain.Parametrages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParametrageBilanRepository extends JpaRepository<ParametrageBilan,Long> {

    ParametrageBilan findByNom(EParametrageBilan nom);
}
