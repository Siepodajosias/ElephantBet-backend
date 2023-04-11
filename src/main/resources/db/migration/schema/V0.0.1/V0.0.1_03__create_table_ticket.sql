CREATE TABLE ticket
(
    id                      INT8 CONSTRAINT ticket_pk PRIMARY KEY,
    ticket_reference         VARCHAR(50)  NOT NULL CONSTRAINT ticket_reference_uk UNIQUE,
    date_of_creation          DATE NOT NULL,
    ticket_price             INT4   NULL,
    amt_won                  INT4  NULL,
    date_when_won            VARCHAR(255) NULL,
    staff_creator_id         VARCHAR(255)  NULL,
    staff_creator_name       VARCHAR(50)   NULL,
    staff_creator_group_id    VARCHAR(50)   NULL,
    staff_creator_group_name  VARCHAR(50)   NULL,
    ticket_status            VARCHAR(50)   NULL,
    staff_pay_group          VARCHAR(50)   NULL
);

CREATE SEQUENCE ticket_id_SEQ INCREMENT BY 50 START 1;
ALTER TABLE ticket
    ALTER COLUMN id SET DEFAULT nextval('ticket_id_seq');
