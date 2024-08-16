CREATE SEQUENCE preferences_seq;

CREATE TABLE preferences (
    id BIGINT PRIMARY KEY DEFAULT nextval('preferences_seq')
);

CREATE TABLE preferences_interests (
    preferences_id BIGINT,
    interest VARCHAR(255),
    PRIMARY KEY (preferences_id, interest),
    FOREIGN KEY (preferences_id) REFERENCES preferences(id)
);

ALTER TABLE users ADD COLUMN preferences_id BIGINT;
ALTER TABLE users ADD CONSTRAINT fk_preferences FOREIGN KEY (preferences_id) REFERENCES preferences(id);
