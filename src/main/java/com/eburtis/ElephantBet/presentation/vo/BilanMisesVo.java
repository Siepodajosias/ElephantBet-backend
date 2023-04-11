package com.eburtis.ElephantBet.presentation.vo;

public class BilanMisesVo {

    private String staffCreatorGroupName;



    private long nombreTicketSuperieur;
    private long totalMiseSuperieur;

    public BilanMisesVo() {
    }

    public BilanMisesVo(String staffCreatorGroupName, long nombreTicketSuperieur, long totalMiseSuperieur) {
        this.staffCreatorGroupName = staffCreatorGroupName;
        this.nombreTicketSuperieur = nombreTicketSuperieur;
        this.totalMiseSuperieur = totalMiseSuperieur;
    }

    public void mettreAJourTicketValeurSuperieure(long nombreTicketSuperieur, long totalMiseSuperieur) {
        this.nombreTicketSuperieur = nombreTicketSuperieur;
        this.totalMiseSuperieur = totalMiseSuperieur;
    }

    public String getStaffCreatorGroupName() {
        return staffCreatorGroupName;
    }

    public long getNombreTicketSuperieur() {
        return nombreTicketSuperieur;
    }

    public long getTotalMiseSuperieur() {
        return totalMiseSuperieur;
    }

}
