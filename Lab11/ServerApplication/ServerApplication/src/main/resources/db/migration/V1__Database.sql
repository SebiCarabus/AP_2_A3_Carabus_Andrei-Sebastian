CREATE TABLE IF NOT EXISTS questions (
    id SERIAL PRIMARY KEY,
    text TEXT NOT NULL,
    option1 VARCHAR(50) NOT NULL,
    option2 VARCHAR(50) NOT NULL,
    option3 VARCHAR(50) NOT NULL,
    option4 VARCHAR(50) NOT NULL,
    correct_option_index SMALLINT NOT NULL CHECK (correct_option_index >= 1 AND correct_option_index <= 4)
    );