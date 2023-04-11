package com.eburtis.ElephantBet.repository.utilisateur;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eburtis.ElephantBet.domain.Utilisateur;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long>,UtilisateurRepositoryCustom {

	@Query("select c from Utilisateur c where c.actif = true and c.username = :username and c.password = :password")
	Optional<Utilisateur> findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

	Optional<Utilisateur> findByUsername(String username);
	Optional<List<Utilisateur>> findByRole(String code);
}

interface  UtilisateurRepositoryCustom{
	Long countUtilisateur(Long id);
}
