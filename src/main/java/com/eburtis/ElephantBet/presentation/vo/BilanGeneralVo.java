package com.eburtis.ElephantBet.presentation.vo;

public class BilanGeneralVo {

    private String staffCreatorGroupName;
    private long nombreTicketVendu;
    private long nombreTicketGagnant;
    private long nombreTicketPending;
    private long totalGains;
    private long totalMise;

    private long balance;

    public BilanGeneralVo() {

    }

    public BilanGeneralVo(String staffCreatorGroupName, long nombreTicketVendu, long nombreTicketGagnant, long nombreTicketPending, long totalGains, long totalMise,long balance) {
        this.staffCreatorGroupName = staffCreatorGroupName;
        this.nombreTicketVendu = nombreTicketVendu;
        this.nombreTicketGagnant = nombreTicketGagnant;
        this.nombreTicketPending = nombreTicketPending;
        this.totalGains = totalGains;
        this.totalMise = totalMise;
        this.balance= balance;
    }

    public String getStaffCreatorGroupName() {
        return staffCreatorGroupName;
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

    public long getBalance() {
        return balance;
    }
}
