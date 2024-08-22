DO $$
BEGIN
    -- Check if the foreign key constraint 'fk_plan' exists
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.table_constraints
        WHERE constraint_name = 'fk_plan'
          AND table_name = 'plan_users'
    ) THEN
        ALTER TABLE plan_users
        ADD CONSTRAINT fk_plan
        FOREIGN KEY (plan_id)
        REFERENCES plan(id);
    END IF;
END $$;

-- Check if the foreign key constraint 'fk_username' exists
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.table_constraints
        WHERE constraint_name = 'fk_username'
          AND table_name = 'plan_users'
    ) THEN
        ALTER TABLE plan_users
        ADD CONSTRAINT fk_username
        FOREIGN KEY (username)
        REFERENCES users(username);
    END IF;
END $$;
