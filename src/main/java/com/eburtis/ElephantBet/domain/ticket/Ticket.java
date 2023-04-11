package com.eburtis.ElephantBet.domain.ticket;


import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Access(AccessType.FIELD)
@Table(name = Ticket.TABLE_NAME)
public class Ticket {

    public static final String TABLE_NAME = "ticket";
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
    private int ticketPrice;

    @Column(name = "amt_won", nullable = true)
    private int amtWon;

    @Column(name = "date_when_won",nullable = true)
    private String dateWhenWon;

    @Column(name = "staff_creator_id", nullable = true)
    private String staffCreatorId;

    @Column(name = "staff_creator_name", nullable = true)
    private String staffCreatorName;

    @Column(name = "staff_creator_group_id", nullable = true)
    private String staffCreatorGroupId;

    @Column(name = "staff_creator_group_name", nullable = true)
    private String staffCreatorGroupName;

    @Column(name = "ticket_status", nullable = true)
    private String ticketStatus;

    @Column(name = "staff_pay_group", nullable = true)
    private String staffPayGroup;


    public Ticket() {
    }

    /**
     * Constructeur avec tout les constructeur
     *
     * @param ticketReference
     * @param dateOfCreation
     * @param ticketPrice
     * @param amtWon
     * @param dateWhenWon
     * @param staffCreatorId
     * @param staffCreator_name
     * @param staffCreator_groupId
     * @param staffCreator_groupName
     * @param ticketStatus
     * @param staffPay_group
     */
    public Ticket(String ticketReference, LocalDate dateOfCreation, int ticketPrice, int amtWon,
                  String dateWhenWon, String staffCreatorId,
                  String staffCreator_name, String staffCreator_groupId,
                  String staffCreator_groupName, String ticketStatus, String staffPay_group) {
        this.ticketReference = ticketReference;
        this.dateOfCreation = dateOfCreation;
        this.ticketPrice = ticketPrice;
        this.amtWon = amtWon;
        this.dateWhenWon = dateWhenWon;
        this.staffCreatorId = staffCreatorId;
        this.staffCreatorName = staffCreator_name;
        this.staffCreatorGroupId = staffCreator_groupId;
        this.staffCreatorGroupName = staffCreator_groupName;
        this.ticketStatus =  ticketStatus;
        this.staffPayGroup = staffPay_group;
    }

    /**
     * Mettre à jour le status du ticket.
     *
     * @param ticketStatus Le nouveau statut du ticket
     */
    public void mettreStatusAjour(String ticketStatus) {
        this.ticketStatus = ticketStatus.trim();
    }

    public void mettreStatusGagnant(String ticketStatus, int amtWon, String dateWhenWon) {
        this.ticketStatus = ticketStatus.trim();
        this.amtWon = amtWon;
        this.dateWhenWon = dateWhenWon;
    }

    /**
     * Vérifie si la date du bilan est inférieure à la date passé en paramètre.
     *
     * @param date la date réference.
     */
    public boolean estInferieureADate(LocalDate date) {
        return getDateOfCreation().isBefore(date);
    }

    /**
     * Vérifie si la date du bilan est supériereure à la date passé en paramètre.
     *
     * @param date la date réference.
     */
    public boolean estSuperieureADate(LocalDate date) {
        return getDateOfCreation().isAfter(date);
    }

    /**
     * Vérifie si la date du bilan est égale à la date passé en paramètre.
     *
     * @param date la date réference.
     */
    public boolean estEgaleADate(LocalDate date) {
        return getDateOfCreation().equals(date);
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

    public int getTicketPrice() {
        return ticketPrice;
    }

    public int getAmtWon() {
        return amtWon;
    }

    public String getDateWhenWon() {
        return dateWhenWon;
    }

    public String getStaffCreatorId() {
        return staffCreatorId;
    }

    public String getStaffCreatorName() {
        return staffCreatorName;
    }

    public String getStaffCreatorGroupId() {
        return staffCreatorGroupId;
    }

    public String getStaffCreatorGroupName() {
        return staffCreatorGroupName;
    }

    public String getStaffPayGroup() {
        return staffPayGroup;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }


}
