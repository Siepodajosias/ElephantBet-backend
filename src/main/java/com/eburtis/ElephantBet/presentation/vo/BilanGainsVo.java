package com.eburtis.ElephantBet.presentation.vo;

import javax.persistence.Column;
import java.time.LocalDate;

public class BilanGainsVo {

    private String staffCreatorGroupName;

    private long nombreGainsSuperieur;

    private long totalGainsSuperieur;

    public BilanGainsVo() {
    }

    public BilanGainsVo(String staffCreatorGroupName,  long nombreGainsSuperieur,  long totalGainsSuperieur) {
        this.staffCreatorGroupName = staffCreatorGroupName;
        this.nombreGainsSuperieur = nombreGainsSuperieur;
        this.totalGainsSuperieur = totalGainsSuperieur;
    }

    public void mettreAJourTicketValeurSuperieure(long nombreGainsSuperieur, long totalGainsSuperieur) {
        this.nombreGainsSuperieur = nombreGainsSuperieur;
        this.totalGainsSuperieur = totalGainsSuperieur;
    }

    public void mettreAJourGroupeName(String staffCreatorGroupName) {
        this.staffCreatorGroupName = staffCreatorGroupName;
    }

    public String getStaffCreatorGroupName() {
        return staffCreatorGroupName;
    }

    public long getNombreGainsSuperieur() {
        return nombreGainsSuperieur;
    }

    public long getTotalGainsSuperieur() {
        return totalGainsSuperieur;
    }
}
