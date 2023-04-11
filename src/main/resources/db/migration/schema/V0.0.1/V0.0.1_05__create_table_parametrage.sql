-- Table de parametrage pour toute les limites definies.
CREATE TABLE parametrage
(
    id   INT8 NOT NULL CONSTRAINT parametrage_pk PRIMARY KEY,
    code VARCHAR(255) NOT NULL,
    limite_debut   INT8,
    limite_fin   INT8,
    groupe     VARCHAR(255) NULL,
    date_debut_limite  DATE NOT NULL,
    date_fin_limite    DATE NOT NULL
);

CREATE SEQUENCE parametrage_id_SEQ INCREMENT BY 50 START 1;
ALTER TABLE parametrage
    ALTER COLUMN id SET DEFAULT nextval('parametrage_id_seq');
