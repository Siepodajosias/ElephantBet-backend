CREATE TABLE bilan_gain
(
    id                      INT8 CONSTRAINT bilan_gain_pk PRIMARY KEY,
    ticket_reference         VARCHAR(50)  NOT NULL CONSTRAINT bilan_gain_reference_uk UNIQUE,
    date_of_creation          DATE NOT NULL,
    ticket_price             INT4   NULL,
    amt_won                  INT4  NULL,
    date_when_won            DATE NULL,
    staff_creator_group_name  VARCHAR(50)   NULL,
    ticket_status            VARCHAR(50)   NULL
);

CREATE SEQUENCE bilan_gain_id_SEQ INCREMENT BY 50 START 1;
ALTER TABLE bilan_gain
    ALTER COLUMN id SET DEFAULT nextval('bilan_gain_id_seq');
