CREATE TABLE bilan_jeu
(
    id   INT8 CONSTRAINT bilan_jeu_pk PRIMARY KEY,
    date_creation    DATE NOT NULL,
    nom_jeu VARCHAR(255) NOT NULL,
    nombre_jeu    INT8   NULL,
    volume_jeu INT8  NULL,
    nombre_gains  INT8 NULL,
    volume_gains  INT8 NULL,
    balance  INT8 NULL,
    nombre_gains_superieur_limite   INT8  NULL,
    nombre_gains_inferieur_limite    INT8   NULL,
    volume_gains_inferieur_limite    INT8   NULL,
    volume_gains_superieur_limite    INT8   NULL

);

CREATE SEQUENCE bilan_jeu_id_SEQ INCREMENT BY 50 START 1;
ALTER TABLE bilan_jeu
    ALTER COLUMN id SET DEFAULT nextval('bilan_jeu_id_seq');
