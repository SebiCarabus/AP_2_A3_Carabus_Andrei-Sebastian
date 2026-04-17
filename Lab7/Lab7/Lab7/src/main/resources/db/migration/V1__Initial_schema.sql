CREATE TABLE IF NOT EXISTS genres (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
    );

CREATE TABLE IF NOT EXISTS movies (
    id INTEGER PRIMARY KEY,
    title VARCHAR(255),
    release_date DATE,
    duration INTEGER,
    score DECIMAL(3,1)
    );

CREATE TABLE IF NOT EXISTS actors (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE
    );

CREATE TABLE IF NOT EXISTS movies_actors (
    movie_id INTEGER REFERENCES movies(id) ON DELETE CASCADE,
    actor_id INTEGER REFERENCES actors(id) ON DELETE CASCADE,
    PRIMARY KEY (movie_id, actor_id)
    );

CREATE TABLE IF NOT EXISTS movies_genres (
    movie_id INTEGER REFERENCES movies(id) ON DELETE CASCADE,
    genre_id INTEGER REFERENCES genres(id) ON DELETE CASCADE,
    PRIMARY KEY (movie_id, genre_id)
    );