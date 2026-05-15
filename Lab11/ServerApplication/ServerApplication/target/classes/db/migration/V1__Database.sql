CREATE TABLE IF NOT EXISTS questions (
    id SERIAL PRIMARY KEY,
    text TEXT NOT NULL,
    option1 VARCHAR(50) NOT NULL,
    option2 VARCHAR(50) NOT NULL,
    option3 VARCHAR(50) NOT NULL,
    option4 VARCHAR(50) NOT NULL,
    correct_option_index SMALLINT NOT NULL CHECK (correct_option_index >= 1 AND correct_option_index <= 4)
    );

CREATE TABLE IF NOT EXISTS players (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);


CREATE TABLE IF NOT EXISTS games (
    id SERIAL PRIMARY KEY,
    start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    max_players SMALLINT NOT NULL
);


CREATE TABLE IF NOT EXISTS results (
    id SERIAL PRIMARY KEY,
    player_id INTEGER NOT NULL REFERENCES players(id),
    game_id INTEGER NOT NULL REFERENCES games(id),
    score INTEGER DEFAULT 0,
    total_response_time BIGINT DEFAULT 0,
    UNIQUE (player_id, game_id)
);
