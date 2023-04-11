package com.eburtis.ElephantBet.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Access(AccessType.FIELD)
@Table(name = Parametrage.TABLE_NAME)
public class Parametrage {
    public static final String TABLE_NAME = "parametrage";
    public static final String TABLE_ID = TABLE_NAME + "_id";
    public static final String TABLE_SEQ = TABLE_ID + "_seq";

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = TABLE_SEQ, sequenceName = TABLE_SEQ)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_SEQ)
    private long id;

    @Enumerated(EnumType.STRING)
    private Parametrages code;

    @Column(name = "limite_debut")
    private Integer limiteDebut;

    @Column(name = "limite_fin")
    private Integer limiteFin;

    @Column(name = "groupe")
    private String groupe;

    @Column(name = "date_debut_limite")
    private LocalDate dateDebutLimite;

    @Column(name = "date_fin_limite")
    private LocalDate dateFinLimite;

    public Parametrage() {
    }
    public Parametrage(Parametrages code, Integer limiteDebut,
                       Integer limiteFin, String groupe, LocalDate dateDebutLimite,
                       LocalDate dateFinLimite) {
        this.code = code;
        this.limiteDebut = limiteDebut;
        this.limiteFin = limiteFin;
        this.groupe = groupe;
        this.dateDebutLimite = dateDebutLimite;
        this.dateFinLimite = dateFinLimite;
    }

    public void mettreAjour(Integer limiteDebut,
                            Integer limiteFin,
                            String groupe,
                            String dateDebutLimite,
                            String dateFinLimite){
        this.limiteDebut = limiteDebut;
        this.limiteFin = limiteFin;
        this.groupe = groupe;
        this.dateDebutLimite = LocalDate.parse(dateDebutLimite);
        this.dateFinLimite = LocalDate.parse(dateFinLimite);

    }
    public void mettreAjourGroupe(String groupe,
                            String dateDebutLimite,
                            String dateFinLimite){
        this.groupe = groupe;
        this.dateDebutLimite = LocalDate.parse(dateDebutLimite);
        this.dateFinLimite = LocalDate.parse(dateFinLimite);
    }

    public void mettreAjourGains(Integer limiteGains,
                            String dateDebutLimite,
                            String dateFinLimite){
        this.limiteDebut = limiteDebut;
        this.dateDebutLimite = LocalDate.parse(dateDebutLimite);
        this.dateFinLimite = LocalDate.parse(dateFinLimite);
    }

    public void mettreAjourMise(Integer limiteTicket,
                            String dateDebutLimite,
                            String dateFinLimite){
        this.limiteDebut = limiteTicket;
        this.dateDebutLimite = LocalDate.parse(dateDebutLimite);
        this.dateFinLimite = LocalDate.parse(dateFinLimite);
    }

    public long getId() {
        return id;
    }

    public Parametrages getCode() {
        return code;
    }

    public String getGroupe() {
        return groupe;
    }

    public LocalDate getDateDebutLimite() {
        return dateDebutLimite;
    }

    public LocalDate getDateFinLimite() {
        return dateFinLimite;
    }

    public Integer getLimiteDebut() {
        return limiteDebut;
    }

    public Integer getLimiteFin() {
        return limiteFin;
    }
}
