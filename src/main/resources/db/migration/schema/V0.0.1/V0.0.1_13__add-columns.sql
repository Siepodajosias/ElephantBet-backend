ALTER TABLE ticket
ADD COLUMN mois  DATE NULL,
ADD COLUMN annee INT4 NULL;

ALTER TABLE bilan
ADD COLUMN mois  DATE NULL,
ADD COLUMN annee INT4 NULL;

ALTER TABLE gain
ADD COLUMN mois  DATE NULL,
ADD COLUMN annee INT4 NULL;