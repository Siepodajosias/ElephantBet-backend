package com.eburtis.ElephantBet.domain;

import javax.persistence.*;

@Entity
@Access(AccessType.FIELD)
@Table(name = ParametrageBilan.TABLE_NAME)
public class ParametrageBilan {
    public static final String TABLE_NAME = "parametrage_bilan";
    public static final String TABLE_ID = TABLE_NAME + "_id";
    public static final String TABLE_SEQ = TABLE_ID + "_seq";

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = TABLE_SEQ, sequenceName = TABLE_SEQ)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_SEQ)
    private long id;

    @Enumerated(EnumType.STRING)
    private EParametrageBilan nom;

    @Column(name = "limite")
    private Integer limite;

    public ParametrageBilan() {
    }

    public ParametrageBilan(long id, EParametrageBilan nom, Integer limite) {
        this.id = id;
        this.nom = nom;
        this.limite = limite;
    }

    public long getId() {
        return id;
    }

    public EParametrageBilan getNom() {
        return nom;
    }

    public Integer getLimite() {
        return limite;
    }
}
