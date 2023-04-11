package com.eburtis.ElephantBet.presentation.vo;

import javax.validation.constraints.NotNull;

public class StatsTickets {
    @NotNull
    private int totalMise;
    @NotNull
    private int totalGains;
    @NotNull
    private int totalPerte;
    @NotNull
    private int nombreGains;
    @NotNull
    private int nombrePertes;
    @NotNull
    private int nombreMises;
}
