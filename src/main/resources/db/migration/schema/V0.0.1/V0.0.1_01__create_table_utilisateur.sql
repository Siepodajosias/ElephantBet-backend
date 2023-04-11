
CREATE TABLE utilisateur
(
    id                      INT8 CONSTRAINT utilisateur_pk PRIMARY KEY,
    nom                     VARCHAR(50)  NOT NULL,
    prenoms                 VARCHAR(255) NOT NULL,
    username                VARCHAR(255) NOT NULL CONSTRAINT username_reference_uk UNIQUE,
    password                VARCHAR(255) NOT NULL,
    actif                   BOOLEAN,
    role                    VARCHAR(255) NOT NULL
);

CREATE SEQUENCE utilisateur_id_SEQ INCREMENT BY 50 START 1;
ALTER TABLE utilisateur
    ALTER COLUMN id SET DEFAULT nextval('utilisateur_id_seq');
