CREATE SEQUENCE credentials_seq;

CREATE TABLE credentials (
    id BIGINT PRIMARY KEY DEFAULT nextval('credentials_seq'),
    password VARCHAR(255),
    email VARCHAR(255) UNIQUE
);

ALTER TABLE users ADD COLUMN credentials_id BIGINT;
ALTER TABLE users ADD CONSTRAINT fk_credentials FOREIGN KEY (credentials_id) REFERENCES credentials(id);
