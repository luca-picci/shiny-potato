CREATE TYPE user_type_enum AS ENUM ('CLIENT', 'MANAGER');

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
     user_type user_type_enum NOT NULL
);

CREATE TABLE venues (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL
);

CREATE TABLE events (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    venue_id INT,
    type VARCHAR(255),
    description TEXT,
    capacity INT,
    booked_seats INT,
    FOREIGN KEY (venue_id) REFERENCES venues (id)
);

CREATE TABLE reviews (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    event_id INT,
    review_text TEXT,
    rating INT,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (event_id) REFERENCES events (id)
);

CREATE TABLE bookings (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          user_id INT NOT NULL,
                          event_id INT NOT NULL,
                          booking_date TIMESTAMP NOT NULL,
                          FOREIGN KEY (user_id) REFERENCES users (id),
                          FOREIGN KEY (event_id) REFERENCES events (id)
);

