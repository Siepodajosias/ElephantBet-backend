package com.eburtis.ElephantBet.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Access(AccessType.FIELD)
@Table(name = BilanJeu.TABLE_NAME)
public class BilanJeu {

    public static final String TABLE_NAME = "bilan_jeu";
    public static final String TABLE_ID = TABLE_NAME + "_id";
    public static final String TABLE_SEQ = TABLE_ID + "_seq";
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = TABLE_SEQ, sequenceName = TABLE_SEQ)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_SEQ)
    private long id;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @Column(name = "nom_jeu", nullable = false)
    private String nomJeu;


    @Column(name = "nombre_jeu")
    private long nombreJeu;

    @Column(name = "volume_jeu")
    private long volumeJeu;

    @Column(name = "nombre_gains")
    private long nombreGains;
    @Column(name = "volume_gains")
    private long volumeGains;

    @Column(name = "balance")
    private long balance;

    @Column(name = "nombre_gains_superieur_limite")
    private long nombreGainsSuperieurLimite;

    @Column(name = "nombre_gains_inferieur_limite")
    private long nombreGainsInferieurLimite;


    @Column(name = "volume_gains_inferieur_limite")
    private long volumeGainsInferieurLimite;

    @Column(name = "volume_gains_superieur_limite")
    private long volumeGainsSuperieurLimite;

    public BilanJeu() {
    }

    public long getId() {
        return id;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public String getNomJeu() {
        return nomJeu;
    }

    public BilanJeu(LocalDate dateCreation, String nomJeu, long nombreJeu, long volumeJeu,
                    long nombreGains, long volumeGains, long balance, long nombreGainsSuperieurLimite,
                    long nombreGainsInferieurLimite, long volumeGainsInferieurLimite, long volumeGainsSuperieurLimite) {
        this.dateCreation = dateCreation;
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

    public void mettreAjour(BilanJeu bilanJeu) {
        this.dateCreation = bilanJeu.getDateCreation();
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

    public long getNombreJeu() {
        return nombreJeu;
    }

    public long getVolumeJeu() {
        return volumeJeu;
    }

    public long getVolumeGains() {
        return volumeGains;
    }

    public long getNombreGains() {
        return nombreGains;
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

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setNomJeu(String nomJeu) {
        this.nomJeu = nomJeu;
    }

    public void setNombreJeu(long nombreJeu) {
        this.nombreJeu = nombreJeu;
    }

    public void setVolumeJeu(long volumeJeu) {
        this.volumeJeu = volumeJeu;
    }

    public void setNombreGains(long nombreGains) {
        this.nombreGains = nombreGains;
    }

    public void setVolumeGains(long volumeGains) {
        this.volumeGains = volumeGains;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public void setNombreGainsSuperieurLimite(long nombreGainsSuperieurLimite) {
        this.nombreGainsSuperieurLimite = nombreGainsSuperieurLimite;
    }

    public void setNombreGainsInferieurLimite(long nombreGainsInferieurLimite) {
        this.nombreGainsInferieurLimite = nombreGainsInferieurLimite;
    }

    public void setVolumeGainsInferieurLimite(long volumeGainsInferieurLimite) {
        this.volumeGainsInferieurLimite = volumeGainsInferieurLimite;
    }

    public void setVolumeGainsSuperieurLimite(long volumeGainsSuperieurLimite) {
        this.volumeGainsSuperieurLimite = volumeGainsSuperieurLimite;
    }
}
