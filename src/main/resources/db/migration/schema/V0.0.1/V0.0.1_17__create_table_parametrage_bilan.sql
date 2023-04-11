CREATE TABLE parametrage_bilan
(
    id   INT8 NOT NULL CONSTRAINT parametrage_bilan_pk PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    limite   INT8);

CREATE SEQUENCE parametrage_bilan_id_SEQ INCREMENT BY 50 START 1;
ALTER TABLE parametrage_bilan
    ALTER COLUMN id SET DEFAULT nextval('parametrage_bilan_id_seq');
