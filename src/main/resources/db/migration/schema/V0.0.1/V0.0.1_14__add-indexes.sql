CREATE INDEX idx_date_of_creation_ticket ON ticket(date_of_creation);
CREATE INDEX idx_ticket_reference_ticket ON ticket(ticket_reference);
CREATE INDEX idx_staff_creator_group_id_ticket ON ticket(staff_creator_group_id);
CREATE INDEX idx_staff_creator_group_name_ticket ON ticket(staff_creator_group_name);

CREATE INDEX idx_staff_creator_group_name_bilan ON bilan(staff_creator_group_name);
CREATE INDEX idx_staff_creator_group_id_bilan ON bilan(staff_creator_group_id);
CREATE INDEX idx_date_of_creation_bilan ON bilan(date_of_creation);
CREATE INDEX idx_nombre_ticket_vendu_bilan ON bilan(nombre_ticket_vendu);
CREATE INDEX idx_nombre_ticket_gagnant_bilan ON bilan(nombre_ticket_gagnant);
CREATE INDEX idx_nombre_ticket_pending_bilan ON bilan(nombre_ticket_pending);
CREATE INDEX idx_total_gains_bilan ON bilan(total_gains);
CREATE INDEX idx_total_mise_bilan ON bilan(total_mise);

CREATE INDEX idx_staff_creator_group_name_gain ON gain(staff_creator_group_name);
CREATE INDEX idx_date_of_creation_gain ON gain(date_of_creation);
CREATE INDEX idx_ticket_price_gain ON gain(ticket_price);
CREATE INDEX idx_amt_won_gain ON gain(amt_won);