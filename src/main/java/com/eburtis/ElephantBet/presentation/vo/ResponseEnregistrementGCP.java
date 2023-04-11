package com.eburtis.ElephantBet.presentation.vo;

import com.eburtis.ElephantBet.domain.ticket.Ticket;
import com.google.cloud.storage.Blob;

import java.util.List;

public class ResponseEnregistrementGCP {
    private List<Ticket> tickets ;
    private Blob fichier;

    public ResponseEnregistrementGCP(List<Ticket> tickets, Blob fichier) {
        this.tickets = tickets;
        this.fichier = fichier;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public Blob getFichier() {
        return fichier;
    }
}
