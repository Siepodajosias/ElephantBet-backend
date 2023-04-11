package com.eburtis.ElephantBet.presentation.vo;

import javax.persistence.Column;

public class BilanJournalierVo {
    private String dateCreation;

    private long nombreTicketVendu;

    private long nombreTicketGagnant;

    private long nombreTicketPending;

    private long totalGains;

    private long totalMise;

    /**
     * Constructeur par défaut.
     */
    public BilanJournalierVo() {
    }

    public BilanJournalierVo(String dateCreation, long nombreTicketVendu, long nombreTicketGagnant, long nombreTicketPending, long totalGains, long totalMise) {
        this.dateCreation = dateCreation;
        this.nombreTicketVendu = nombreTicketVendu;
        this.nombreTicketGagnant = nombreTicketGagnant;
        this.nombreTicketPending = nombreTicketPending;
        this.totalGains = totalGains;
        this.totalMise = totalMise;
    }

    public String getDateCreation() {
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

    public long getTotalMise() {
        return totalMise;
    }

    public long getTotalGains() {
        return totalGains;
    }
}
