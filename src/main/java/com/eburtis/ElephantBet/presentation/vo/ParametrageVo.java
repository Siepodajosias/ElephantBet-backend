package com.eburtis.ElephantBet.presentation.vo;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class ParametrageVo {

    private Long id;
    private String code;
    @NotNull
    private Integer limiteDebut;
    @NotNull
    private Integer limiteFin;
    @NotNull
    private String groupe;
    @NotNull
    private String dateDebutLimite;
    @NotNull
    private String dateFinLimite;


    public ParametrageVo() {
    }

    public ParametrageVo(Long id,String code, Integer limiteDebut, Integer limiteTicket, String groupe, String dateDebutLimite, String dateFinLimite) {
        this.id=id;
        this.code = code;
        this.limiteDebut = limiteDebut;
        this.limiteFin = limiteFin;
        this.groupe = groupe;
        this.dateDebutLimite = dateDebutLimite;
        this.dateFinLimite = dateFinLimite;
    }

    public ParametrageVo(String code, Integer limiteDebut, Integer limiteFin, String groupe, String dateDebutLimite, String dateFinLimite) {
        this.code = code;
        this.limiteDebut = limiteDebut;
        this.limiteFin = limiteFin;
        this.groupe = groupe;
        this.dateDebutLimite = dateDebutLimite;
        this.dateFinLimite = dateFinLimite;
    }

    public ParametrageVo(Long id) {
        this.id = id;
    }

    public void miseAjourParametrage(Integer limiteDebut, Integer limiteTicket, String groupe, String dateDebutLimite, String dateFinLimite) {
        this.limiteDebut = limiteDebut;
        this.limiteFin = limiteFin;
        this.groupe = groupe;
        this.dateDebutLimite = dateDebutLimite;
        this.dateFinLimite = dateFinLimite;
    }


    public Integer getLimiteDebut() {
        return limiteDebut;
    }

    public Integer getLimiteFin() {
        return limiteFin;
    }

    public String getGroupe() {
        return groupe;
    }

    public String getDateDebutLimite() {
        return dateDebutLimite;
    }

    public String getDateFinLimite() {
        return dateFinLimite;
    }

    public String getCode() {
        return code;
    }

    public Long getId() {
        return id;
    }
}
