CREATE SEQUENCE users_seq;

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    date_of_birth DATE,
    name VARCHAR(255)
);
