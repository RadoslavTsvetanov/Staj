 CREATE TABLE date_window (
    id BIGSERIAL PRIMARY KEY,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL
);

CREATE TABLE plan (
    id BIGSERIAL PRIMARY KEY,
    est_cost INT NOT NULL,
    budget INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    date_window_id BIGINT,
    CONSTRAINT fk_date_window
        FOREIGN KEY (date_window_id)
        REFERENCES date_window(id)
);

CREATE TABLE plan_users (
    plan_id BIGINT,
    user_id BIGINT,
    PRIMARY KEY (plan_id, user_id),
    CONSTRAINT fk_plan
        FOREIGN KEY (plan_id)
        REFERENCES plan(id),
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);
