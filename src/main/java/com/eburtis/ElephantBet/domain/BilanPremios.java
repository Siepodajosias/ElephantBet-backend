package com.eburtis.ElephantBet.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Access(AccessType.FIELD)
@Table(name = BilanPremios.TABLE_NAME)
public class BilanPremios {
    public static final String TABLE_NAME = "bilan_premios";
    public static final String TABLE_ID = TABLE_NAME + "_id";
    public static final String TABLE_SEQ = TABLE_ID + "_seq";
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = TABLE_SEQ, sequenceName = TABLE_SEQ)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_SEQ)
    private long id;

    @Column(name = "date_evenement")
    private LocalDate dateEvenement;

    @Column(name = "agence", nullable = false)
    private String agence;

    @Column(name = "valeur")
    private long valeur;

    public BilanPremios() {
    }

    public BilanPremios(LocalDate dateEvenement, String agence, long valeur) {
        this.dateEvenement = dateEvenement;
        this.agence = agence;
        this.valeur = valeur;
    }

    public void mettreAjour(BilanPremios bilanPremios) {
        this.dateEvenement = bilanPremios.getDateEvenement();
        this.agence = bilanPremios.getAgence();
        this.valeur = bilanPremios.getValeur();
    }

    public long getId() {
        return id;
    }

    public LocalDate getDateEvenement() {
        return dateEvenement;
    }

    public String getAgence() {
        return agence;
    }

    public long getValeur() {
        return valeur;
    }

}
