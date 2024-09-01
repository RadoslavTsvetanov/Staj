DO $$
BEGIN
    -- Check if the column 'location' exists
    IF EXISTS (SELECT 1 FROM information_schema.columns
               WHERE table_name = 'memories' AND column_name = 'location') THEN
        -- Check if the column 'place' does not exist
        IF NOT EXISTS (SELECT 1 FROM information_schema.columns
                       WHERE table_name = 'memories' AND column_name = 'place') THEN
            ALTER TABLE memories RENAME COLUMN location TO place;
        END IF;
    END IF;
END $$;
