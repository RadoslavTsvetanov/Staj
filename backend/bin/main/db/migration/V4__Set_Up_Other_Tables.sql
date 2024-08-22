CREATE TABLE history (
    id BIGSERIAL PRIMARY KEY
);

CREATE TABLE date_window (
    id BIGSERIAL PRIMARY KEY,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL
);

CREATE TABLE plan (
    id BIGSERIAL PRIMARY KEY,
    est_cost INT NOT NULL,
    budget INT NOT NULL,
    name VARCHAR(255) NOT NULL UNIQUE,
    date_window_id BIGINT,
    history_id BIGINT,
    CONSTRAINT fk_date_window
        FOREIGN KEY (date_window_id)
        REFERENCES date_window(id),
    CONSTRAINT fk_history
        FOREIGN KEY (history_id)
        REFERENCES history(id)
);

CREATE TABLE location (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age_restriction BOOLEAN,
    type VARCHAR(255),
    cost INT
);

CREATE TABLE place (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    plan_id BIGINT,
    date_window_id BIGINT,
    CONSTRAINT fk_plan
        FOREIGN KEY (plan_id)
        REFERENCES plan(id),
    CONSTRAINT fk_date_window
        FOREIGN KEY (date_window_id)
        REFERENCES date_window(id)
);

CREATE TABLE place_location (
    id BIGSERIAL PRIMARY KEY,
    place_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    day INT CHECK (day >= 1 AND day <= 28),
    CONSTRAINT fk_place
        FOREIGN KEY (place_id)
        REFERENCES place(id),
    CONSTRAINT fk_location
        FOREIGN KEY (location_id)
        REFERENCES location(id)
);

CREATE TABLE memories (
    id BIGSERIAL PRIMARY KEY,
    image VARCHAR(255) NOT NULL,
    date DATE NOT NULL,
    location VARCHAR(255) NOT NULL,
    history_id BIGINT,
    CONSTRAINT fk_history
        FOREIGN KEY (history_id)
        REFERENCES history(id)
);

CREATE TABLE plan_users (
    plan_id BIGINT,
    username VARCHAR(20) NOT NULL,
    PRIMARY KEY (plan_id, username),
    CONSTRAINT fk_plan
        FOREIGN KEY (plan_id)
        REFERENCES plan(id),
    CONSTRAINT fk_username
        FOREIGN KEY (username)
        REFERENCES users(username)
);
