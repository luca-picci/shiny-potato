-- Inserisci gli utenti
INSERT INTO users (name, email, password, user_type) VALUES
('Giulia', 'giulia@example.com', 'password1', 'CLIENT'),
('Lorenzo', 'lorenzo@example.com', 'password2', 'MANAGER');

-- Inserisci i luoghi
INSERT INTO venues (name, address) VALUES
('Stadio Olimpico', 'Roma'),
('Sala Conferenze Centrale', 'Milan');

-- Inserisci gli eventi
INSERT INTO events (title, date, venue_id) VALUES 
('Concerto di Vasco Rossi', '2025-02-10', (SELECT id FROM venues WHERE name = 'Stadio Olimpico')), 
('Convegno Tecnologico', '2025-02-20', (SELECT id FROM venues WHERE name = 'Sala Conferenze Centrale'));

-- Inserisci le recensioni
INSERT INTO reviews (user_id, event_id, comment, rating) 
VALUES 
    ((SELECT id FROM users WHERE name = 'Giulia'), 
     (SELECT id FROM events WHERE title= 'Concerto di Vasco Rossi'), 
     'Concerto spettacolare, emozioni incredibili!', 5), 
    ((SELECT id FROM users WHERE name = 'Lorenzo'), 
     (SELECT id FROM events WHERE title= 'Convegno Tecnologico'), 
     'Organizzazione impeccabile e interventi interessanti.', 4);

