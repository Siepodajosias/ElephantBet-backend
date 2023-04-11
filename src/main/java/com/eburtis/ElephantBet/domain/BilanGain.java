package com.eburtis.ElephantBet.domain;

import com.eburtis.ElephantBet.domain.ticket.Ticket;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Access(AccessType.FIELD)
@Table(name = BilanGain.TABLE_NAME)
public class BilanGain {
    public static final String TABLE_NAME = "bilan_gain";
    public static final String TABLE_ID = TABLE_NAME + "_id";
    public static final String TABLE_SEQ = TABLE_ID + "_seq";
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = TABLE_SEQ, sequenceName = TABLE_SEQ)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_SEQ)
    private long id;

    @Column(name = "ticket_reference")
    private String ticketReference;

    @Column(name = "date_of_creation",nullable = true)
    private LocalDate dateOfCreation;

    @Column(name = "ticket_price", nullable = true)
    private Integer ticketPrice;

    @Column(name = "amt_won", nullable = true)
    private Integer amtWon;

    @Column(name = "date_when_won",nullable = true)
    private LocalDate dateWhenWon;

    @Column(name = "staff_creator_group_name", nullable = true)
    private String staffCreatorGroupName;

    @Column(name = "ticket_status", nullable = true)
    private String ticketStatus;

    public BilanGain() {
    }

    public BilanGain(Ticket ticket) {
        this.ticketReference = ticket.getTicketReference();
        this.dateOfCreation = ticket.getDateOfCreation();
        this.ticketPrice = ticket.getTicketPrice();
        this.amtWon = ticket.getAmtWon();
        this.dateWhenWon = LocalDate.parse(ticket.getDateWhenWon());
        this.staffCreatorGroupName = ticket.getStaffCreatorGroupName();
        this.ticketStatus = ticket.getTicketStatus();
    }

    /**
     * Constructeur parametré
     * @param ticketReference
     * @param dateOfCreation
     * @param ticketPrice
     * @param amtWon
     * @param dateWhenWon
     * @param staffCreatorGroupName
     * @param ticketStatus
     */
    public BilanGain(String ticketReference, LocalDate dateOfCreation, Integer ticketPrice, Integer amtWon, LocalDate dateWhenWon, String staffCreatorGroupName, String ticketStatus) {
        this.ticketReference = ticketReference;
        this.dateOfCreation = dateOfCreation;
        this.ticketPrice = ticketPrice;
        this.amtWon = amtWon;
        this.dateWhenWon = dateWhenWon;
        this.staffCreatorGroupName = staffCreatorGroupName;
        this.ticketStatus = ticketStatus;
    }


    /**
     * Vérifie si la date du bilan est inférieure à la date passé en paramètre.
     *
     * @param date la date réference.
     */
    public boolean estInferieureADate(LocalDate date) {
        return getDateWhenWon().isBefore(date);
    }

    /**
     * Vérifie si la date du bilan est supériereure à la date passé en paramètre.
     *
     * @param date la date réference.
     */
    public boolean estSuperieureADate(LocalDate date) {
        return getDateWhenWon().isAfter(date);
    }

    /**
     * Vérifie si la date du bilan est égale à la date passé en paramètre.
     *
     * @param date la date réference.
     */
    public boolean estEgaleADate(LocalDate date) {
        return getDateWhenWon().equals(date);
    }

    public long getId() {
        return id;
    }

    public String getTicketReference() {
        return ticketReference;
    }

    public LocalDate getDateOfCreation() {
        return dateOfCreation;
    }

    public Integer getTicketPrice() {
        return ticketPrice;
    }

    public Integer getAmtWon() {
        return amtWon;
    }

    public LocalDate getDateWhenWon() {
        return dateWhenWon;
    }

    public String getStaffCreatorGroupName() {
        return staffCreatorGroupName;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }
}
