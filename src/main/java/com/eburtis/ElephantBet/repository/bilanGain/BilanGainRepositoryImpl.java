package com.eburtis.ElephantBet.repository.bilanGain;

import com.eburtis.ElephantBet.domain.Parametrage;
import com.eburtis.ElephantBet.repository.bilanGain.BilanGainRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;

public class BilanGainRepositoryImpl implements BilanGainRepositoryCustom {


    @PersistenceContext
    private EntityManager entityManager;


    /**
     * Requete de la liste des bilans de gains pour l'année
     * @param annee
     * @param parametrages
     * @return
     */
    @Override
    public List<Object[]> listBilanGainPourAnnee(Integer annee, List<Parametrage> parametrages) {
        StringBuilder exclusionPourRequete = new StringBuilder();
        exclusionPourRequete.append("select staff_creator_group_name from bilan_gain where date_part('year', date_when_won) = :annee"+ " ");
        if (!parametrages.isEmpty()) {
            for (Parametrage parametrage : parametrages) {
                String exclusion = "and ((date_when_won not between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        " or ((date_when_won between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        "and (amt_won not between" + " '" + parametrage.getLimiteDebut().toString() + "' " + "and" + " '" + parametrage.getLimiteFin().toString() + "')" +
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
     * Requete de la liste des bilans de gains pour l'année supérieures à la limite
     * @param annee
     * @param limite
     * @param parametrages
     * @return
     */
    @Override
    public List<Object[]> listBilanGainAvecPrixSuperieurALimitePourAnnee(Integer annee, Integer limite, List<Parametrage> parametrages) {
        StringBuilder exclusionPourRequete = new StringBuilder();
        exclusionPourRequete.append("select staff_creator_group_name, count(*), sum(amt_won) from bilan_gain where amt_won > :limite and date_part('year', date_when_won) = :annee" + " ");
        if (!parametrages.isEmpty()) {
            for (Parametrage parametrage : parametrages) {
                String exclusion = "and ((date_when_won not between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        " or ((date_when_won between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        "and (amt_won not between" + " '" + parametrage.getLimiteDebut().toString() + "' " + "and" + " '" + parametrage.getLimiteFin().toString() + "')" +
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
     * Requete de la liste des bilans de gains pour l'année inférieures à la limite
     * @param annee
     * @param limite
     * @param parametrages
     * @return
     */
    @Override
    public List<Object[]> listBilanGainAvecPrixInferieurALimitePourAnnee(Integer annee, Integer limite, List<Parametrage> parametrages) {
        StringBuilder exclusionPourRequete = new StringBuilder();
        exclusionPourRequete.append("select staff_creator_group_name, count(*), sum(amt_won) from bilan_gain where amt_won <= :limite and date_part('year', date_when_won) = :annee" + " ");

        if (!parametrages.isEmpty()) {
            for (Parametrage parametrage : parametrages) {
                String exclusion = "and ((date_when_won not between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        " or ((date_when_won between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        "and (amt_won not between" + " '" + parametrage.getLimiteDebut().toString() + "' " + "and" + " '" + parametrage.getLimiteFin().toString() + "')" +
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
     * Requete de la liste des bilans de gains pour le mois
     * @param annee
     * @param mois
     * @param parametrages
     * @return
     */
    @Override
    public List<Object[]> listBilanGainPourMois(Integer annee, Integer mois, List<Parametrage> parametrages) {
        StringBuilder exclusionPourRequete = new StringBuilder();
        exclusionPourRequete.append("select staff_creator_group_name from bilan_gain where date_part('year', date_when_won) = :annee and date_part('month', date_when_won) = :mois" + " ");
        if (!parametrages.isEmpty()) {
            for (Parametrage parametrage : parametrages) {
                String exclusion = "and ((date_when_won not between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        " or ((date_when_won between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        "and (amt_won not between" + " '" + parametrage.getLimiteDebut().toString() + "' " + "and" + " '" + parametrage.getLimiteFin().toString() + "')" +
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
     * Requete de la liste des bilans de gains pour le mois supérieures à la limite
     * @param annee
     * @param mois
     * @param limite
     * @param parametrages
     * @return
     */
    @Override
    public List<Object[]> listBilanGainAvecPrixSuperieurALimitePourMois(Integer annee, Integer mois, Integer limite, List<Parametrage> parametrages) {
        StringBuilder exclusionPourRequete = new StringBuilder();
        exclusionPourRequete.append("select staff_creator_group_name, count(*), sum(amt_won) from bilan_gain where amt_won > :limite and date_part('year', date_when_won) = :annee and date_part('month', date_when_won) = :mois" + " ");
        if (!parametrages.isEmpty()) {
            for (Parametrage parametrage : parametrages) {
                String exclusion = "and ((date_when_won not between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        " or ((date_when_won between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        "and (amt_won not between" + " '" + parametrage.getLimiteDebut().toString() + "' " + "and" + " '" + parametrage.getLimiteFin().toString() + "')" +
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
     * Requete de la liste des bilans de gains pour le mois inférieures à la limite
     * @param annee
     * @param mois
     * @param limite
     * @param parametrages
     * @return
     */
    @Override
    public List<Object[]> listBilanGainAvecPrixInferieurALimitePourMois(Integer annee, Integer mois, Integer limite, List<Parametrage> parametrages) {
        StringBuilder exclusionPourRequete = new StringBuilder();
        exclusionPourRequete.append("select staff_creator_group_name, count(*), sum(amt_won) from bilan_gain where amt_won <= :limite and date_part('year', date_when_won) = :annee and date_part('month', date_when_won) = :mois" + " ");
        if (!parametrages.isEmpty()) {
            for (Parametrage parametrage : parametrages) {
                String exclusion = "and ((date_when_won not between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        " or ((date_when_won between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        "and (amt_won not between" + " '" + parametrage.getLimiteDebut().toString() + "' " + "and" + " '" + parametrage.getLimiteFin().toString() + "')" +
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
     * Requete de la liste des bilans de gains pour la période
     * @param dateDebut
     * @param dateFin
     * @param parametrages
     * @return
     */
    @Override
    public List<Object[]> listBilanGainPourPeriode(LocalDate dateDebut, LocalDate dateFin, List<Parametrage> parametrages) {
        StringBuilder exclusionPourRequete = new StringBuilder();
        exclusionPourRequete.append("select staff_creator_group_name from bilan_gain where date_when_won between :dateDebut and :dateFin" + " ");
        if (!parametrages.isEmpty()) {
            for (Parametrage parametrage : parametrages) {
                String exclusion = "and ((date_when_won not between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        " or ((date_when_won between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        "and (amt_won not between" + " '" + parametrage.getLimiteDebut().toString() + "' " + "and" + " '" + parametrage.getLimiteFin().toString() + "')" +
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
     * Requete de la liste des bilans de gains pour la période supérieure à la limite
     * @param dateDebut
     * @param dateFin
     * @param limite
     * @param parametrages
     * @return
     */
    @Override
    public List<Object[]> listBilanGainAvecPrixSuperieurALimitePourPeriode(LocalDate dateDebut, LocalDate dateFin, Integer limite, List<Parametrage> parametrages) {
        StringBuilder exclusionPourRequete = new StringBuilder();
        exclusionPourRequete.append("select staff_creator_group_name, count(*), sum(amt_won) from bilan_gain where amt_won > :limite and date_when_won between :dateDebut and :dateFin" + " ");
        if (!parametrages.isEmpty()) {
            for (Parametrage parametrage : parametrages) {
                String exclusion = "and ((date_when_won not between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        " or ((date_when_won between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        "and (amt_won not between" + " '" + parametrage.getLimiteDebut().toString() + "' " + "and" + " '" + parametrage.getLimiteFin().toString() + "')" +
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
     * Requete de la liste des bilans de gains pour la période supérieure à la limite
     * @param dateDebut
     * @param dateFin
     * @param limite
     * @param parametrages
     * @return
     */
    @Override
    public List<Object[]> listBilanGainAvecPrixInferieurALimitePourPeriode(LocalDate dateDebut, LocalDate dateFin, Integer limite, List<Parametrage> parametrages) {
        StringBuilder exclusionPourRequete = new StringBuilder();
        exclusionPourRequete.append("select staff_creator_group_name, count(*), sum(amt_won) from bilan_gain where amt_won <= :limite and date_when_won between :dateDebut and :dateFin" + " ");
        if (!parametrages.isEmpty()) {
            for (Parametrage parametrage : parametrages) {
                String exclusion = "and ((date_when_won not between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        " or ((date_when_won between" + " '" + parametrage.getDateDebutLimite().toString() + "' " + "and" + " '" + parametrage.getDateFinLimite().toString() + "')" +
                        "and (amt_won not between" + " '" + parametrage.getLimiteDebut().toString() + "' " + "and" + " '" + parametrage.getLimiteFin().toString() + "')" +
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


