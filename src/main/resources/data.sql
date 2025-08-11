-- Заповнення таблиці users
INSERT INTO users (first_name, last_name, email, password, role) VALUES
                                                                          ('Ivan', 'Petrenko', 'ivan@example.com', '$2a$10$RBTxB4Cn3MMFJ/d9zVVwP.qyJSwYsE6MX4wL2j/qG1ilsHoP3zG62', 'USER'),
                                                                          ('Admin', 'Adminenko', 'admin@example.com', '$2a$10$RBTxB4Cn3MMFJ/d9zVVwP.qyJSwYsE6MX4wL2j/qG1ilsHoP3zG62', 'ADMIN');

-- Заповнення таблиці book
INSERT INTO book (title, description, publication_year, category, available, author_name) VALUES
                                                                                                  ('The Hobbit', 'Fantasy novel by J.R.R. Tolkien', 1937, 'FANTASY', false, 'J.R.R. Tolkien'),
                                                                                                  ('A Brief History of Time', 'Popular-science book on cosmology', 1988, 'SCIENCE', true, 'Stephen Hawking'),
                                                                                                  ('1984', 'Dystopian novel by George Orwell', 1949, 'FICTION', true, 'George Orwell');

-- Заповнення таблиці rent
INSERT INTO rent (user_id, book_id, rent_date, return_date, status) VALUES
                                                                            (1, 1, '2025-07-01', NULL, 'ACTIVE'),
                                                                            (1, 3, '2025-06-15', '2025-07-01', 'RETURNED');
