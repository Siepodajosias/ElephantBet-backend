package com.eburtis.ElephantBet.repository.ticket;

import com.eburtis.ElephantBet.domain.Parametrage;
import com.eburtis.ElephantBet.repository.ticket.TicketRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;

public class TicketRepositoryImpl implements TicketRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Recupère les tickets pour une année
     *
     * @param annee        année selectionnée
     * @param parametrages les parametrages d'exclusions
     * @return les tickets de l'année
     */
    @Override
    public List<Object[]> listTicketPourAnnee(Integer annee, List<Parametrage> parametrages) {
        StringBuilder exclusionPourRequete = new StringBuilder();
        exclusionPourRequete.append("select staff_creator_group_name from ticket where date_part('year', date_of_creation) = :annee" + " ");
        if (!parametrages.isEmpty()) {
            for (Parametrage parametrage : parametrages) {
                String exclusion = " and ((date_of_creation not between" + " '" + parametrage.getDateDebutLimite().toString() +
                        "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        " or ((date_of_creation between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" +
                        parametrage.getDateFinLimite().toString() + "')" +
                        "and (ticket_price not between" + " '" + parametrage.getLimiteDebut().toString() + "' " + "and" + " '" +
                        parametrage.getLimiteFin().toString() + "')" +
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
     * Requete de la liste des bilans de mise pour l'année supérieure à la limite
     *
     * @param annee
     * @param limite
     * @param parametrages
     * @return
     */
    @Override
    public List<Object[]> listTicketAvecPrixSuperieurALimitePourAnnee(Integer annee, Integer limite, List<Parametrage> parametrages) {
        StringBuilder exclusionPourRequete = new StringBuilder();
        exclusionPourRequete.append("select staff_creator_group_name, count(*), sum(ticket_price) from ticket where ticket_price > :limite and date_part('year', date_of_creation) = :annee" + " ");
        if (!parametrages.isEmpty()) {
            for (Parametrage parametrage : parametrages) {
                String exclusion = "and ((date_of_creation not between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        " or ((date_of_creation between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        "and (ticket_price not between" + " '" + parametrage.getLimiteDebut().toString() + "' " + "and" + " '" + parametrage.getLimiteFin().toString() + "')" +
                        "))" + "";
                exclusionPourRequete.append(String.join(" ", exclusion));
            }
        }
        exclusionPourRequete.append(" " + "group by staff_creator_group_name;");
        Query query = entityManager.createNativeQuery(exclusionPourRequete.toString());
        if (limite != null) {
            query.setParameter("limite", limite);
        }
        if (annee != null) {
            query.setParameter("annee", annee);
        }
        return query.getResultList();
    }

    /**
     * Requete de la liste des bilans de mise pour l'année inférieure à la limite
     *
     * @param annee
     * @param limite
     * @param parametrages
     * @return
     */
    @Override
    public List<Object[]> listTicketAvecPrixInferieurALimitePourAnnee(Integer annee, Integer limite, List<Parametrage> parametrages) {
        StringBuilder exclusionPourRequete = new StringBuilder();
        exclusionPourRequete.append("select staff_creator_group_name, count(*), sum(ticket_price) from ticket where ticket_price < :limite and date_part('year', date_of_creation) = :annee" + " ");
        if (!parametrages.isEmpty()) {
            for (Parametrage parametrage : parametrages) {
                String exclusion = "and ((date_of_creation not between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        " or ((date_of_creation between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        "and (ticket_price not between" + " '" + parametrage.getLimiteDebut().toString() + "' " + "and" + " '" + parametrage.getLimiteFin().toString() + "')" +
                        "))" + "";
                exclusionPourRequete.append(String.join(" ", exclusion));
            }
        }
        exclusionPourRequete.append(" " + "group by staff_creator_group_name;");
        Query query = entityManager.createNativeQuery(exclusionPourRequete.toString());
        if (limite != null) {
            query.setParameter("limite", limite);
        }
        if (annee != null) {
            query.setParameter("annee", annee);
        }
        return query.getResultList();
    }

    /**
     * Requete de la liste des bilans de mise pour le mois
     *
     * @param annee
     * @param mois
     * @param parametrages
     * @return
     */
    @Override
    public List<Object[]> listTicketPourMois(Integer annee, Integer mois, List<Parametrage> parametrages) {
        StringBuilder exclusionPourRequete = new StringBuilder();
        exclusionPourRequete.append("select staff_creator_group_name from ticket where date_part('year', date_of_creation) = :annee and date_part('month', date_of_creation) = :mois" + " ");
        if (!parametrages.isEmpty()) {
            for (Parametrage parametrage : parametrages) {
                String exclusion = "and ((date_of_creation not between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        " or ((date_of_creation between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        "and (ticket_price not between" + " '" + parametrage.getLimiteDebut().toString() + "' " + "and" + " '" + parametrage.getLimiteFin().toString() + "')" +
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
     * Requete de la liste des bilans de mise pour le mois supérieure à la limite
     *
     * @param annee
     * @param mois
     * @param limite
     * @param parametrages
     * @return
     */
    @Override
    public List<Object[]> listTicketAvecPrixSuperieurALimitePourMois(Integer annee, Integer mois, Integer limite, List<Parametrage> parametrages) {
        StringBuilder exclusionPourRequete = new StringBuilder();
        exclusionPourRequete.append("select staff_creator_group_name, count(*), sum(ticket_price) from ticket where ticket_price > :limite and date_part('year', date_of_creation) = :annee and date_part('month', date_of_creation) = :mois" + " ");
        if (!parametrages.isEmpty()) {
            for (Parametrage parametrage : parametrages) {
                String exclusion = "and ((date_of_creation not between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        " or ((date_of_creation between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        "and (ticket_price not between" + " '" + parametrage.getLimiteDebut().toString() + "' " + "and" + " '" + parametrage.getLimiteFin().toString() + "')" +
                        "))" + "";
                exclusionPourRequete.append(String.join(" ", exclusion));
            }
        }
        exclusionPourRequete.append(" " + "group by staff_creator_group_name;");
        Query query = entityManager.createNativeQuery(exclusionPourRequete.toString());
        if (limite != null) {
            query.setParameter("limite", limite);
        }
        if (annee != null) {
            query.setParameter("annee", annee);
        }
        if (mois != null) {
            query.setParameter("mois", mois);
        }
        return query.getResultList();
    }

    /**
     * Requete de la liste des bilans de mise pour le mois inférieure à la limite
     *
     * @param annee
     * @param mois
     * @param limite
     * @param parametrages
     * @return
     */
    @Override
    public List<Object[]> listTicketAvecPrixInferieurALimitePourMois(Integer annee, Integer mois, Integer limite, List<Parametrage> parametrages) {
        StringBuilder exclusionPourRequete = new StringBuilder();
        exclusionPourRequete.append("select staff_creator_group_name, count(*), sum(ticket_price) from ticket where ticket_price < :limite and date_part('year', date_of_creation) = :annee and date_part('month', date_of_creation) = :mois" + " ");
        if (!parametrages.isEmpty()) {
            for (Parametrage parametrage : parametrages) {
                String exclusion = "and ((date_of_creation not between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        " or ((date_of_creation between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        "and (ticket_price not between" + " '" + parametrage.getLimiteDebut().toString() + "' " + "and" + " '" + parametrage.getLimiteFin().toString() + "')" +
                        "))" + "";
                exclusionPourRequete.append(String.join(" ", exclusion));
            }
        }
        exclusionPourRequete.append(" " + "group by staff_creator_group_name;");
        Query query = entityManager.createNativeQuery(exclusionPourRequete.toString());
        if (limite != null) {
            query.setParameter("limite", limite);
        }
        if (annee != null) {
            query.setParameter("annee", annee);
        }
        if (mois != null) {
            query.setParameter("mois", mois);
        }
        return query.getResultList();
    }

    /**
     * Requete de la liste des bilans de mise pour la période
     *
     * @param dateDebut
     * @param dateFin
     * @param parametrages
     * @return
     */
    @Override
    public List<Object[]> listTicketPourPeriode(LocalDate dateDebut, LocalDate dateFin, List<Parametrage> parametrages) {
        StringBuilder exclusionPourRequete = new StringBuilder();
        exclusionPourRequete.append("select staff_creator_group_name from ticket where date_of_creation between :dateDebut and :dateFin" + " ");
        if (!parametrages.isEmpty()) {
            for (Parametrage parametrage : parametrages) {
                String exclusion = "and ((date_of_creation not between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        " or ((date_of_creation between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        "and (ticket_price not between" + " '" + parametrage.getLimiteDebut().toString() + "' " + "and" + " '" + parametrage.getLimiteFin().toString() + "')" +
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

    /**
     * Requete de la liste des bilans de mise pour la période supérieure à la limite
     *
     * @param dateDebut
     * @param dateFin
     * @param limite
     * @param parametrages
     * @return
     */
    @Override
    public List<Object[]> listTicketAvecPrixSuperieurALimitePourPeriode(LocalDate dateDebut, LocalDate dateFin, Integer limite, List<Parametrage> parametrages) {
        StringBuilder exclusionPourRequete = new StringBuilder();
        exclusionPourRequete.append("select staff_creator_group_name, count(*), sum(ticket_price) from ticket where ticket_price > :limite and date_of_creation between :dateDebut and :dateFin" + " ");
        if (!parametrages.isEmpty()) {
            for (Parametrage parametrage : parametrages) {
                String exclusion = "and ((date_of_creation not between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        " or ((date_of_creation between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        "and (ticket_price not between" + " '" + parametrage.getLimiteDebut().toString() + "' " + "and" + " '" + parametrage.getLimiteFin().toString() + "')" +
                        "))" + "";
                exclusionPourRequete.append(String.join(" ", exclusion));
            }
        }
        exclusionPourRequete.append(" " + "group by staff_creator_group_name;");
        Query query = entityManager.createNativeQuery(exclusionPourRequete.toString());
        if (limite != null) {
            query.setParameter("limite", limite);
        }
        if (dateDebut != null) {
            query.setParameter("dateDebut", dateDebut);
        }
        if (dateFin != null) {
            query.setParameter("dateFin", dateFin);
        }
        return query.getResultList();
    }

    /**
     * Requete de la liste des bilans de mise pour la période inférieure à la limite
     *
     * @param dateDebut
     * @param dateFin
     * @param limite
     * @param parametrages
     * @return
     */
    @Override
    public List<Object[]> listTicketAvecPrixInferieurALimitePourPeriode(LocalDate dateDebut, LocalDate dateFin, Integer limite, List<Parametrage> parametrages) {
        StringBuilder exclusionPourRequete = new StringBuilder();
        exclusionPourRequete.append("select staff_creator_group_name, count(*), sum(ticket_price) from ticket where ticket_price < :limite and date_of_creation between :dateDebut and :dateFin" + " ");
        if (!parametrages.isEmpty()) {
            for (Parametrage parametrage : parametrages) {
                String exclusion = "and ((date_of_creation not between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        " or ((date_of_creation between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        "and (ticket_price not between" + " '" + parametrage.getLimiteDebut().toString() + "' " + "and" + " '" + parametrage.getLimiteFin().toString() + "')" +
                        "))" + "";
                exclusionPourRequete.append(String.join(" ", exclusion));
            }
        }
        exclusionPourRequete.append(" " + "group by staff_creator_group_name;");
        Query query = entityManager.createNativeQuery(exclusionPourRequete.toString());
        if (limite != null) {
            query.setParameter("limite", limite);
        }
        if (dateDebut != null) {
            query.setParameter("dateDebut", dateDebut);
        }
        if (dateFin != null) {
            query.setParameter("dateFin", dateFin);
        }
        return query.getResultList();
    }
}
