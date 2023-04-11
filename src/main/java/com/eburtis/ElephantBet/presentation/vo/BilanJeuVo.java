package com.eburtis.ElephantBet.presentation.vo;

import com.eburtis.ElephantBet.domain.BilanJeu;

public class BilanJeuVo {

    private String nomJeu;
    private long nombreJeu;
    private long volumeJeu;

    private long nombreGains;

    private long volumeGains;

    private long balance;

    private long nombreGainsSuperieurLimite;

    private long nombreGainsInferieurLimite;

    private long volumeGainsInferieurLimite;

    private long volumeGainsSuperieurLimite;

    public BilanJeuVo() {
    }

    public BilanJeuVo(String nomJeu, long nombreJeu, long volumeJeu,
                      long nombreGains, long volumeGains, long balance, long nombreGainsSuperieurLimite,
                      long nombreGainsInferieurLimite, long volumeGainsInferieurLimite, long volumeGainsSuperieurLimite) {
        this.nomJeu = nomJeu;
        this.nombreJeu = nombreJeu;
        this.volumeJeu = volumeJeu;
        this.nombreGains = nombreGains;
        this.volumeGains = volumeGains;
        this.balance = balance;
        this.nombreGainsSuperieurLimite = nombreGainsSuperieurLimite;
        this.nombreGainsInferieurLimite = nombreGainsInferieurLimite;
        this.volumeGainsInferieurLimite = volumeGainsInferieurLimite;
        this.volumeGainsSuperieurLimite = volumeGainsSuperieurLimite;
    }

    public BilanJeuVo(BilanJeu bilanJeu) {
        this.nomJeu = bilanJeu.getNomJeu();
        this.nombreJeu = bilanJeu.getNombreJeu();
        this.volumeJeu = bilanJeu.getVolumeJeu();
        this.nombreGains = bilanJeu.getNombreGains();
        this.volumeGains = bilanJeu.getVolumeGains();
        this.balance = bilanJeu.getBalance();
        this.nombreGainsSuperieurLimite = bilanJeu.getNombreGainsSuperieurLimite();
        this.nombreGainsInferieurLimite = bilanJeu.getNombreGainsInferieurLimite();
        this.volumeGainsInferieurLimite = bilanJeu.getVolumeGainsInferieurLimite();
        this.volumeGainsSuperieurLimite = bilanJeu.getVolumeGainsSuperieurLimite();
    }

    public String getNomJeu() {
        return nomJeu;
    }

    public long getNombreJeu() {
        return nombreJeu;
    }

    public long getVolumeJeu() {
        return volumeJeu;
    }

    public long getNombreGains() {
        return nombreGains;
    }

    public long getVolumeGains() {
        return volumeGains;
    }

    public long getBalance() {
        return balance;
    }

    public long getNombreGainsSuperieurLimite() {
        return nombreGainsSuperieurLimite;
    }

    public long getNombreGainsInferieurLimite() {
        return nombreGainsInferieurLimite;
    }

    public long getVolumeGainsInferieurLimite() {
        return volumeGainsInferieurLimite;
    }

    public long getVolumeGainsSuperieurLimite() {
        return volumeGainsSuperieurLimite;
    }
}
