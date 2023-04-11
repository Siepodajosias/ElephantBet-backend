package com.eburtis.ElephantBet.domain;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Access(AccessType.FIELD)
@Table(name = Fichier.TABLE_NAME)
public class Fichier {
    public static final String TABLE_NAME = "fichier";
    public static final String TABLE_ID = TABLE_NAME + "_id";
    public static final String TABLE_SEQ = TABLE_ID + "_seq";

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = TABLE_SEQ, sequenceName = TABLE_SEQ)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_SEQ)
    private long id;

    @Column(name = "nom_fichier")
    private String nomFichier;

    @Column(name = "nombre_ligne")
    private Integer nombreLigne;

    @Column(name = "taille_fichier")
    private long tailleFichier;

    @Column(name = "date_enregistrement")
    private LocalDateTime dateEnregistrement;

    public Fichier() {
    }

    public Fichier(String nomDuFichier, Integer nombreDeLigne, long tailleDuFichier, LocalDateTime dateEnregistrement) {
        this.nomFichier = nomDuFichier;
        this.nombreLigne = nombreDeLigne;
        this.tailleFichier = tailleDuFichier;
        this.dateEnregistrement = dateEnregistrement;
    }

    public long getId() {
        return id;
    }

    public String getNomDuFichier() {
        return nomFichier;
    }

    public Integer getNombreDeLigne() {
        return nombreLigne;
    }

    public long getTailleDuFichier() {
        return tailleFichier;
    }

    public LocalDateTime getDateEnregistrement() {
        return dateEnregistrement;
    }
}
