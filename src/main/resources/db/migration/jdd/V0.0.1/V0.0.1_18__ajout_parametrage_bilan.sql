INSERT INTO public.parametrage_bilan
(id, nom,limite)
VALUES(nextval('parametrage_bilan_id_seq'),'GAINS', 250000),
(nextval('parametrage_bilan_id_seq'), 'MISES', 50000);