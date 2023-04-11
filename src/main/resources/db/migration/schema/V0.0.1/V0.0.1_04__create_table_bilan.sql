CREATE TABLE bilan
(
    id   INT8 CONSTRAINT bilan_pk PRIMARY KEY,
    date_of_creation    DATE NOT NULL,
    staff_creator_group_name VARCHAR(255) NOT NULL,
    staff_creator_group_id VARCHAR(255) NOT NULL,
    nombre_ticket_vendu    INT8   NULL,
    nombre_ticket_gagnant INT8  NULL,
    nombre_ticket_pending  INT8 NULL,
    nombre_ticket_lost  INT8 NULL,
    total_gains   INT8  NULL,
    total_mise    INT8   NULL
);

CREATE SEQUENCE bilan_id_SEQ INCREMENT BY 50 START 1;
ALTER TABLE bilan
    ALTER COLUMN id SET DEFAULT nextval('bilan_id_seq');
