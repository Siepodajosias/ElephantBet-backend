CREATE TABLE gain
(
    id                      INT8 CONSTRAINT gain_pk PRIMARY KEY,
    ticket_reference         VARCHAR(50)  NOT NULL CONSTRAINT gain_reference_uk UNIQUE,
    date_of_creation          DATE NOT NULL,
    ticket_price             INT4   NULL,
    amt_won                  INT4  NULL,
    date_when_won            DATE NULL,
    staff_creator_group_name  VARCHAR(50)   NULL,
    ticket_status            VARCHAR(50)   NULL
);

CREATE SEQUENCE gain_id_SEQ INCREMENT BY 50 START 1;
ALTER TABLE gain
    ALTER COLUMN id SET DEFAULT nextval('gain_id_seq');
