DROP TABLE IF EXISTS rent CASCADE;
DROP TABLE IF EXISTS book CASCADE;
DROP TABLE IF EXISTS users CASCADE;

DROP TYPE IF EXISTS rent_status CASCADE;
DROP TYPE IF EXISTS category CASCADE;
DROP TYPE IF EXISTS role CASCADE;

CREATE TYPE role AS ENUM ('USER', 'ADMIN');

CREATE TABLE users (
                        id SERIAL PRIMARY KEY,
                        first_name VARCHAR(100) NOT NULL,
                        last_name VARCHAR(100) NOT NULL,
                        email VARCHAR(255) NOT NULL UNIQUE,
                        password VARCHAR(255) NOT NULL,
                        role role NOT NULL
);

CREATE TYPE category AS ENUM ('FICTION', 'NONFICTION', 'SCIENCE', 'FANTASY', 'HISTORY', 'BIOGRAPHY', 'OTHER');

CREATE TABLE book (
                      id SERIAL PRIMARY KEY,
                      title VARCHAR(255) NOT NULL,
                      description TEXT,
                      publication_year INT NOT NULL,
                      category category NOT NULL,
                      available BOOLEAN NOT NULL,
                      author_name VARCHAR(255) NOT NULL
);

CREATE TYPE rent_status AS ENUM ('ACTIVE', 'RETURNED', 'OVERDUE');

CREATE TABLE rent (
                      id SERIAL PRIMARY KEY,
                      user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                      book_id INT NOT NULL REFERENCES book(id) ON DELETE CASCADE,
                      rent_date DATE NOT NULL,
                      return_date DATE,
                      status rent_status NOT NULL
);
