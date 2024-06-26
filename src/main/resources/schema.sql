CREATE TABLE IF NOT EXISTS objects
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS object_counts
(
    id        SERIAL PRIMARY KEY,
    object_id INTEGER   NOT NULL REFERENCES objects (id) ON DELETE CASCADE,
    count     INTEGER   NOT NULL,
    timestamp TIMESTAMP NOT NULL
);