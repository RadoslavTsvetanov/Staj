CREATE SEQUENCE credentials_seq;
CREATE SEQUENCE users_seq;

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    age INT,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE credentials (
    id BIGINT PRIMARY KEY DEFAULT nextval('credentials_seq'),
    password VARCHAR(255),
    email VARCHAR(255) UNIQUE
);

ALTER TABLE users ADD COLUMN credentials_id BIGINT;
ALTER TABLE users ADD CONSTRAINT fk_credentials FOREIGN KEY (credentials_id) REFERENCES credentials(id);
