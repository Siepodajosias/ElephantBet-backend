package com.eburtis.ElephantBet.domain.ticket;

/**
 * L'enum pour les différents statuts du ticket (PENDING, LOST, WIN).
 */
public enum StatutTicket {
    PENDING,
    LOST,
    WIN;

    public static class Values {
        public static final String PENDING = "pending";
        public static final String LOST = "lost";
        public static final String WIN = "win";
    }

}
