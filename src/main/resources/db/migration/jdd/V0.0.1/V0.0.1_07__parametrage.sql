INSERT INTO public.parametrage
(id, code,limite_debut, limite_fin, groupe, date_debut_limite, date_fin_limite)
VALUES(nextval('parametrage_id_seq'),'GROUPES_ET_DATES', NULL, NULL, '4198', '2022-10-20', '2022-10-21'),
(nextval('parametrage_id_seq'), 'LIMITE_TICKETS', 10000, 20000, NULL, '2022-10-20', '2022-10-21'),
(nextval('parametrage_id_seq'), 'LIMITES_GAINS', 10000, 20000, NULL, '2022-10-20', '2022-10-21');