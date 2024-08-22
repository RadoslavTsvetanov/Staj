ALTER TABLE plan_users RENAME TO plan_users_old;

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

INSERT INTO plan_users (plan_id, username)
SELECT pu.plan_id, u.username
FROM plan_users_old pu
JOIN users u ON pu.user_id = u.id;

DROP TABLE plan_users_old;

SELECT * FROM plan_users;
