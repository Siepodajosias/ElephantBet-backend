package com.eburtis.ElephantBet.repository.bilanGeneral;

import com.eburtis.ElephantBet.domain.Parametrage;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;

public class BilanGeneralRepositoryImpl implements BilanGeneralRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Requete de la liste des bilans généraux pour l'année
     * @param annee
     * @param parametrages
     * @return
     */
    @Override
    public List<Object[]> listBilanPourAnnee(Integer annee, List<Parametrage> parametrages) {
        StringBuilder exclusionPourRequete = new StringBuilder();
        exclusionPourRequete.append("select staff_creator_group_name, sum(nombre_ticket_vendu) as nbre_ticket_vendu, " +
                "sum(total_mise) as t_mise, sum(nombre_ticket_gagnant) as nbre_ticket_gagnant," +
                " sum(total_gains) as t_gains," +
                " sum(nombre_ticket_pending) as nbre_ticket_pending" +
                " from bilan where date_part('year', date_of_creation) = :annee " + " ");
        if (!parametrages.isEmpty()) {
            for (Parametrage parametrage : parametrages) {
                String exclusion = "and ((date_of_creation not between" + " '" + parametrage.getDateDebutLimite().toString() + "' " +
                        "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        "or ((date_of_creation between" + " '" + parametrage.getDateDebutLimite().toString() +
                        "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        "and (staff_creator_group_id not in ('"+ parametrage.getGroupe() +"'))"+
                        "))" + "";
                exclusionPourRequete.append(String.join(" ", exclusion));
            }
        }
        exclusionPourRequete.append(" " + "group by staff_creator_group_name;");
        Query query = entityManager.createNativeQuery(exclusionPourRequete.toString());
        if (annee != null) {
            query.setParameter("annee", annee);
        }
        return query.getResultList();
    }

    /**
     * Requete de la liste des bilans généraux pour le mois
     * @param annee
     * @param mois
     * @param parametrages
     * @return
     */
    @Override
    public List<Object[]> listBilanPourMois(Integer annee, Integer mois, List<Parametrage> parametrages) {
        StringBuilder exclusionPourRequete = new StringBuilder();
        exclusionPourRequete.append("select staff_creator_group_name, sum(nombre_ticket_vendu) as nbre_ticket_vendu, " +
                        "sum(total_mise) as t_mise, sum(nombre_ticket_gagnant) as nbre_ticket_gagnant," +
                        " sum(total_gains) as t_gains," +
                        " sum(nombre_ticket_pending) as nbre_ticket_pending" +
                        " from bilan where date_part('year', date_of_creation) = :annee and date_part('month', date_of_creation) = :mois");
        if (!parametrages.isEmpty()) {
            for (Parametrage parametrage : parametrages) {
                String exclusion = " and ((date_of_creation not between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" +
                        parametrage.getDateFinLimite().toString() + "')" +
                        " or ((date_of_creation between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" +
                        parametrage.getDateFinLimite().toString() + "')" +
                        " and (staff_creator_group_id not in ('"+ parametrage.getGroupe() +"'))"+
                        "))" + "";
                exclusionPourRequete.append(String.join(" ", exclusion));
            }
        }
        exclusionPourRequete.append(" " + "group by staff_creator_group_name;");
        Query query = entityManager.createNativeQuery(exclusionPourRequete.toString());
        if (annee != null) {
            query.setParameter("annee", annee);
        }
        if (mois != null) {
            query.setParameter("mois", mois);
        }
        return query.getResultList();
    }


    /**
     * Requete de la liste des bilans généraux pour la période
     * @param dateDebut
     * @param dateFin
     * @param parametrages
     * @return
     */
    @Override
    public List<Object[]> listBilanPourPeriode(LocalDate dateDebut, LocalDate dateFin, List<Parametrage> parametrages) {
        StringBuilder exclusionPourRequete = new StringBuilder();
        exclusionPourRequete.append("select staff_creator_group_name, sum(nombre_ticket_vendu) as nbre_ticket_vendu, " +
                        "sum(total_mise) as t_mise, sum(nombre_ticket_gagnant) as nbre_ticket_gagnant," +
                        " sum(total_gains) as t_gains," +
                        " sum(nombre_ticket_pending) as nbre_ticket_pending from bilan " +
                        "where date_of_creation between :dateDebut and :dateFin");
        if (!parametrages.isEmpty()) {
            for (Parametrage parametrage : parametrages) {
                String exclusion = " and ((date_of_creation not between" + " '" + parametrage.getDateDebutLimite().toString() + "' " +
                        "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        " or ((date_of_creation between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" +
                        " '" + parametrage.getDateFinLimite().toString() + "')" +
                        " and (staff_creator_group_id not in ('"+ parametrage.getGroupe() +"'))"+
                        "))" + "";
                exclusionPourRequete.append(String.join(" ", exclusion));
            }
        }
        exclusionPourRequete.append(" " + "group by staff_creator_group_name;");
        Query query = entityManager.createNativeQuery(exclusionPourRequete.toString());
        if (dateDebut != null) {
            query.setParameter("dateDebut", dateDebut);
        }
        if (dateFin != null) {
            query.setParameter("dateFin", dateFin);
        }
        return query.getResultList();
    }
}


