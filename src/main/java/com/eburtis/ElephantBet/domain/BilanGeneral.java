package com.eburtis.ElephantBet.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Access(AccessType.FIELD)
@Table(name = BilanGeneral.TABLE_NAME)
public class BilanGeneral {

    public static final String TABLE_NAME = "bilan";
    public static final String TABLE_ID = TABLE_NAME + "_id";
    public static final String TABLE_SEQ = TABLE_ID + "_seq";
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = TABLE_SEQ, sequenceName = TABLE_SEQ)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_SEQ)
    private long id;

    @Column(name = "date_of_creation")
    private LocalDate dateCreation;

    @Column(name = "staff_creator_group_name", nullable = true)
    private String staffCreatorGroupName;

    @Column(name = "staff_creator_group_id", nullable = true)
    private String staffCreatorGroupId;

    @Column(name = "nombre_ticket_vendu")
    private long nombreTicketVendu;

    @Column(name = "nombre_ticket_gagnant")
    private long nombreTicketGagnant;

    @Column(name = "nombre_ticket_pending")
    private long nombreTicketPending;

    @Column(name = "nombre_ticket_lost")
    private long nombreTicketLost;

    @Column(name = "total_gains")
    private long totalGains;


    @Column(name = "total_mise")
    private long totalMise;

    /**
     * Constructeur par défaut.
     */
    public BilanGeneral() {

    }

    /**
     * Constructeur paramétré
     *
     * @param dateCreation        La date de création du ticket
     * @param nombreTicketVendu   Le nombre de ticket vendu
     * @param nombreTicketGagnant Le nombre de ticket gagnant
     * @param nombreTicketPending Le nombre de ticket en pending
     * @param totalGains          le total des gains
     * @param totalMise           le total des mises
     */
    public BilanGeneral(LocalDate dateCreation,
                        String staffCreatorGroupName,
                        String staffCreatorGroupId,
                        long nombreTicketVendu,
                        long nombreTicketGagnant,
                        long nombreTicketPending,
                        long nombreTicketLost,
                        long totalGains,
                        long totalMise) {
        this.dateCreation = dateCreation;
        this.staffCreatorGroupName = staffCreatorGroupName;
        this.staffCreatorGroupId =staffCreatorGroupId;
        this.nombreTicketVendu = nombreTicketVendu;
        this.nombreTicketGagnant = nombreTicketGagnant;
        this.nombreTicketPending = nombreTicketPending;
        this.nombreTicketLost = nombreTicketLost;
        this.totalGains = totalGains;
        this.totalMise = totalMise;
    }

    /**
     * Mettre à jour les informations sur le bilan de la journé
     *
     * @param nombreTicketGagnant Le nombre de ticket gagnant
     * @param nombreTicketPending Le nombre de ticket en pending
     * @param totalGains          le total des gains
     */
    public void mettreAJourBilan(
                            long nombreTicketGagnant, long nombreTicketPending,
                            long nombreTicketLost,
                            long totalGains) {
        this.nombreTicketGagnant = this.nombreTicketGagnant + nombreTicketGagnant;
        this.nombreTicketLost = this.nombreTicketLost + nombreTicketLost;
        this.nombreTicketPending = (this.nombreTicketPending + nombreTicketPending) - (nombreTicketGagnant + nombreTicketLost);
        this.totalGains = this.totalGains + totalGains;
    }

    public void mettreAJourBilanAvecNouveau(
            long nombreTicketVendu,
            long nombreTicketGagnant, long nombreTicketPending,
            long nombreTicketLost,
            long totalGains,long totalMise) {
        this.nombreTicketVendu =  nombreTicketVendu;
        this.nombreTicketGagnant = nombreTicketGagnant;
        this.nombreTicketLost =  nombreTicketLost;
        this.nombreTicketPending =  nombreTicketPending;
        this.totalGains = totalGains;
        this.totalMise = totalMise;
    }

    /**
     * Vérifie si la date du bilan est inférieure à la date passé en paramètre.
     *
     * @param date la date réference.
     */
    public boolean estInferieureADate(LocalDate date) {
        return getDateCreation().isBefore(date);
    }

    /**
     * Vérifie si la date du bilan est supériereure à la date passé en paramètre.
     *
     * @param date la date réference.
     */
    public boolean estSuperieureADate(LocalDate date) {
        return getDateCreation().isAfter(date);
    }

    /**
     * Vérifie si la date du bilan est égale à la date passé en paramètre.
     *
     * @param date la date réference.
     */
    public boolean estEgaleADate(LocalDate date) {
        return getDateCreation().equals(date);
    }

    public long getId() {
        return id;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }


    public long getNombreTicketVendu() {
        return nombreTicketVendu;
    }

    public long getNombreTicketGagnant() {
        return nombreTicketGagnant;
    }

    public long getNombreTicketPending() {
        return nombreTicketPending;
    }

    public long getTotalGains() {
        return totalGains;
    }

    public long getTotalMise() {
        return totalMise;
    }

    public String getStaffCreatorGroupName() {
        return staffCreatorGroupName;
    }

    public String getStaffCreatorGroupId() {
        return staffCreatorGroupId;
    }
}
