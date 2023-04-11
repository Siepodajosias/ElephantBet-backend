INSERT INTO public.utilisateur
(id,nom, prenoms, username, "password", actif, role )
VALUES(nextval('utilisateur_id_seq'),'traore', 'Abdoul-aziz', 'tralus', '$2a$10$c6rTdoMdajHowLr1oedPEOzyvgRKRPmye/MQbY8IIfcPMUXrjCP2y', true,'ADMIN');
