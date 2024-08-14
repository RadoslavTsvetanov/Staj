CREATE SEQUENCE location_seq;

CREATE TABLE location (
    id BIGINT PRIMARY KEY DEFAULT nextval('location_seq'),
    name VARCHAR(255) NOT NULL,
    age_restriction BOOLEAN,
    type VARCHAR(255),
    cost INT
);

CREATE TABLE plan_locations (
    plan_id BIGINT,
    location_id BIGINT,
    PRIMARY KEY (plan_id, location_id),
    CONSTRAINT fk_plan
        FOREIGN KEY (plan_id)
        REFERENCES plan(id),
    CONSTRAINT fk_location
        FOREIGN KEY (location_id)
        REFERENCES location(id)
);
