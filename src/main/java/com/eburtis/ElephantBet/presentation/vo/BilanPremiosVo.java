package com.eburtis.ElephantBet.presentation.vo;

public class BilanPremiosVo {
    private String agence;
    private long valeurTotal;
    private long numero;

    public BilanPremiosVo() {
    }

    public BilanPremiosVo(String agence, long valeurTotal, long numero) {
        this.agence = agence;
        this.valeurTotal = valeurTotal;
        this.numero = numero;
    }

    public String getAgence() {
        return agence;
    }

    public long getValeurTotal() {
        return valeurTotal;
    }

    public long getNumero() {
        return numero;
    }
}
