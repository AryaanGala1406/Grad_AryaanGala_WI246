CREATE TABLE sites (
    site_id SERIAL PRIMARY KEY,
    type VARCHAR(20),
    length INT,
    width INT,
    occupied BOOLEAN,
    user_id INT REFERENCES users(user_id)
);

CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    name VARCHAR(50),
    password VARCHAR(30),
    role VARCHAR(10)
);

CREATE TABLE maintenance (
    maintenance_id SERIAL PRIMARY KEY,
    site_id INT REFERENCES sites(site_id),
    year INT,
    amount INT,
    paid_on DATE
);

CREATE TABLE update_details_request (
    details_req_id SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(user_id),
    new_name VARCHAR(50),
    status VARCHAR(10), -- PENDING / APPROVED / REJECTED
    requested_on DATE
);

CREATE TABLE update_requests (
    req_id SERIAL PRIMARY KEY,
    site_id INT REFERENCES sites(site_id),
    user_id INT REFERENCES users(user_id),
    new_owner_name VARCHAR(50),
    new_site_type VARCHAR(20),
    status VARCHAR(10), -- PENDING / APPROVED / REJECTED
    requested_on DATE
);

INSERT INTO users VALUES(System.getenv("ADMIN_USERNAME"), System.getenv("ADMIN_PASSWORD"), System.getenv("ADMIN_PASSWORD"));