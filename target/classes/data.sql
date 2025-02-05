-- Inserisci gli utenti
INSERT INTO users (name, email, password, user_type) VALUES
('Giulia', 'giulia@example.com', '$2a$10$ZBOT1igi83GlBepKBckyWO4XzVhpKE5kQCqySoKh2t1/IcMnlt.VC', 'CLIENT'),
('Lorenzo', 'lorenzo@example.com', '$2a$10$/Pl5XvtRpbekvJgXImO/4.6qarkDoYB6hXv97OOssdtrfwSdQCu3W', 'MANAGER');

-- Inserisci i luoghi
INSERT INTO venues (name, address) VALUES
('Stadio Olimpico', 'Roma'),
('Sala Conferenze Centrale', 'Milano');

-- Inserisci gli eventi (SENZA capacity e booked_seats)
INSERT INTO events (title, date, venue_id, type, description) VALUES 
('Concerto di Vasco Rossi', '2025-02-10', (SELECT id FROM venues WHERE name = 'Stadio Olimpico'), 'Concerto', 'Il Blasco infiamma lo Stadio Olimpico con i suoi più grandi successi e nuove canzoni, per una serata di rock indimenticabile.'), 
('Convegno Tecnologico', '2025-02-20', (SELECT id FROM venues WHERE name = 'Sala Conferenze Centrale'), 'Convegno', 'Esperti del settore si riuniscono per discutere le ultime tendenze e innovazioni tecnologiche, con presentazioni interattive e workshop pratici.');

-- Aggiorna gli eventi con capacity e booked_seats
UPDATE events
SET capacity = 60000, booked_seats = 55000
WHERE title = 'Concerto di Vasco Rossi';

UPDATE events
SET capacity = 500, booked_seats = 400
WHERE title = 'Convegno Tecnologico';

-- Inserisci le recensioni
INSERT INTO reviews (user_id, event_id, review_text, rating) 
VALUES 
    ((SELECT id FROM users WHERE name = 'Giulia'), 
     (SELECT id FROM events WHERE title= 'Concerto di Vasco Rossi'), 
     'Concerto spettacolare, emozioni incredibili!', 5), 
    ((SELECT id FROM users WHERE name = 'Lorenzo'), 
     (SELECT id FROM events WHERE title= 'Convegno Tecnologico'), 
     'Organizzazione impeccabile e interventi interessanti.', 4);