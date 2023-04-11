package com.eburtis.ElephantBet.repository.utilisateur;

import com.eburtis.ElephantBet.repository.utilisateur.UtilisateurRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

public class UtilisateurRepositoryImpl implements UtilisateurRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long countUtilisateur(Long id) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("count_utlisateurs")
                .registerStoredProcedureParameter(
                        "utilisateurId",
                        Long.class,
                        ParameterMode.IN
                )
                .registerStoredProcedureParameter(
                        "utilisateurCount",
                        Long.class,
                        ParameterMode.OUT
                )
                .setParameter("utilisateurId", id);
        query.execute();
         return (Long) query.getOutputParameterValue("utilisateurCount");

    }
}
