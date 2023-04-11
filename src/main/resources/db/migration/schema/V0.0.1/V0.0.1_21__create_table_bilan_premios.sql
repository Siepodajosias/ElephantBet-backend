CREATE TABLE bilan_premios
(
    id   INT8 CONSTRAINT bilan_premios_pk PRIMARY KEY,
    date_evenement    DATE NOT NULL,
    agence VARCHAR(255) NOT NULL,
    valeur    INT8   NULL
);

CREATE SEQUENCE bilan_premios_id_SEQ INCREMENT BY 50 START 1;
ALTER TABLE bilan_premios
    ALTER COLUMN id SET DEFAULT nextval('bilan_premios_id_seq');
