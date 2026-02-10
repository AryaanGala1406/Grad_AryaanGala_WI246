
CREATE TABLE maintenance (
    maintenance_id SERIAL PRIMARY KEY,
    site_id INT REFERENCES sites(site_id),
    user_id INT REFERENCES users(user_id),
    year INT,
    amount INT,
    paid_on DATE
);

-- transaction database
-- add balance amount inside the maintenance 
-- pending, completed 
-- trigger -> when balace = 0 status to paid or completed



-- USERS
CREATE TABLE IF NOT EXISTS users (
    user_id   SERIAL PRIMARY KEY,
    name      VARCHAR(50)  NOT NULL,
    password  VARCHAR(72)  NOT NULL,  
    role      VARCHAR(10)  NOT NULL
);

INSERT INTO users (name, password, role)
VALUES ('admin', 'admin', 'admin');

SELECT * FROM users;
-- SELECT * FROM users WHERE name=('admin') AND password=('admin')

-- DELETE FROM users;

-- SITES
CREATE TABLE IF NOT EXISTS sites (
    site_id   SERIAL PRIMARY KEY,
    type      VARCHAR(20)  NOT NULL,
    length    INT          NOT NULL CHECK (length > 0),
    width     INT          NOT NULL CHECK (width > 0),
    occupied  BOOLEAN      NOT NULL DEFAULT FALSE,
    user_id INT REFERENCES users(user_id),
    CHECK (type IN ('VILLA', 'APARTMENT', 'INDEPENDENT_HOUSE', 'OPEN_SITE'))
);

-- SELECT * FROM sites;

CREATE OR REPLACE FUNCTION enforce_occupancy_rule()
RETURNS TRIGGER AS $$
BEGIN
    -- Set occupied based on site type
    IF NEW.type IN ('VILLA', 'APARTMENT', 'INDEPENDENT_HOUSE') THEN
        NEW.occupied := TRUE;
    ELSIF NEW.type = 'OPEN_SITE' THEN
        NEW.occupied := FALSE;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER set_occupancy
BEFORE INSERT OR UPDATE ON sites
FOR EACH ROW
EXECUTE FUNCTION enforce_occupancy_rule();

-- MAINTENANCE
CREATE TABLE IF NOT EXISTS maintenance (
    maintenance_id SERIAL PRIMARY KEY,
    site_id   INT REFERENCES sites(site_id) NOT NULL,
    year      INT NOT NULL CHECK (year >= 2000),
    amount    INT NOT NULL CHECK (amount >= 0),
    status        VARCHAR(10)  NOT NULL DEFAULT 'PENDING',
    paid_on   DATE,
    CHECK (status IN ('PENDING','COMPLETED'))
);

-- UPDATE REQUESTS (merged)
CREATE TABLE IF NOT EXISTS update_requests (
    req_id        SERIAL PRIMARY KEY,
    req_type      VARCHAR(20)  NOT NULL,       -- 'DETAILS' or 'SITE'
    user_id       INT          NOT NULL REFERENCES users(user_id),
    site_id       INT          REFERENCES sites(site_id),  -- nullable if DETAILS
    new_name          VARCHAR(50),            -- for DETAILS
    new_owner_name    VARCHAR(50),            -- for SITE-owner change (free text)
    new_site_type     VARCHAR(20),            -- for SITE-type change
    status        VARCHAR(10)  NOT NULL DEFAULT 'PENDING',
    requested_on  TIMESTAMPTZ  NOT NULL DEFAULT now(),
    CHECK (status IN ('PENDING','APPROVED','REJECTED')),
    CHECK (
      (req_type = 'DETAILS' AND site_id IS NULL AND new_name IS NOT NULL)
      OR
      (req_type = 'SITE' AND site_id IS NOT NULL AND (new_owner_name IS NOT NULL OR new_site_type IS NOT NULL))
    )
);

-- SELECT * FROM update_requests;
-- First 10 sites: 40x60
INSERT INTO sites (type, length, width, user_id) VALUES
('VILLA',               40, 60, NULL),
('APARTMENT',           40, 60, NULL),
('INDEPENDENT_HOUSE',   40, 60, NULL),
('OPEN_SITE',           40, 60, NULL),
('VILLA',               40, 60, NULL),
('APARTMENT',           40, 60, NULL),
('INDEPENDENT_HOUSE',   40, 60, NULL),
('OPEN_SITE',           40, 60, NULL),
('VILLA',               40, 60, NULL),
('APARTMENT',           40, 60, NULL);

-- Next 10 sites: 30x50
INSERT INTO sites (type, length, width, user_id) VALUES
('INDEPENDENT_HOUSE',   30, 50, NULL),
('OPEN_SITE',           30, 50, NULL),
('VILLA',               30, 50, NULL),
('APARTMENT',           30, 50, NULL),
('INDEPENDENT_HOUSE',   30, 50, NULL),
('OPEN_SITE',           30, 50, NULL),
('VILLA',               30, 50, NULL),
('APARTMENT',           30, 50, NULL),
('INDEPENDENT_HOUSE',   30, 50, NULL),
('OPEN_SITE',           30, 50, NULL);

-- Last 15 sites: 30x40
INSERT INTO sites (type, length, width, user_id) VALUES
('VILLA',               30, 40, NULL),
('APARTMENT',           30, 40, NULL),
('INDEPENDENT_HOUSE',   30, 40, NULL),
('OPEN_SITE',           30, 40, NULL),
('VILLA',               30, 40, NULL),
('APARTMENT',           30, 40, NULL),
('INDEPENDENT_HOUSE',   30, 40, NULL),
('OPEN_SITE',           30, 40, NULL),
('VILLA',               30, 40, NULL),
('APARTMENT',           30, 40, NULL),
('INDEPENDENT_HOUSE',   30, 40, NULL),
('OPEN_SITE',           30, 40, NULL),
('VILLA',               30, 40, NULL),
('APARTMENT',           30, 40, NULL),
('INDEPENDENT_HOUSE',   30, 40, NULL);

-- TRUNCATE TABLE maintenance, update_requests, sites RESTART IDENTITY CASCADE;

