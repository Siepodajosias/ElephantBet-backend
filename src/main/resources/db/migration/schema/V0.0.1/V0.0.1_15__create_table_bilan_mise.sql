CREATE TABLE bilan_mise
(
    id                      INT8 CONSTRAINT bilan_mise_pk PRIMARY KEY,
    ticket_reference         VARCHAR(50)  NOT NULL CONSTRAINT bilan_mise_reference_uk UNIQUE,
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

CREATE SEQUENCE bilan_mise_id_SEQ INCREMENT BY 50 START 1;
ALTER TABLE bilan_mise
    ALTER COLUMN id SET DEFAULT nextval('bilan_mise_id_seq');
