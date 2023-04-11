
CREATE TABLE fichier
(
    id                      INT8 CONSTRAINT fichier_pk PRIMARY KEY,
    nom_fichier             VARCHAR(50)  NOT NULL,
    nombre_ligne            INT4 NOT NULL,
    taille_fichier          INT4 NOT NULL,
    date_enregistrement     TIMESTAMP NOT NULL
);

CREATE SEQUENCE fichier_id_SEQ INCREMENT BY 50 START 1;
ALTER TABLE fichier
    ALTER COLUMN id SET DEFAULT nextval('fichier_id_seq');
