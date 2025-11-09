INSERT INTO books (title, author, price, isbn, description, cover_image)
VALUES
  ('Test Book 1', 'One', 10.1, '978-0000000001', 'Description for Book 1', 'https://example.com/cover1.jpg'),
  ('Test Book 2', 'Two', 10.1, '978-0000000002', 'Description for Book 2', 'https://example.com/cover2.jpg'),
  ('Test Book 3', 'Three', 10.1, '978-0000000003', 'Description for Book 3', 'https://example.com/cover3.jpg');

  INSERT INTO books_categories (book_id, category_id) VALUES (1, 1);
  INSERT INTO books_categories (book_id, category_id) VALUES (2, 2);
  INSERT INTO books_categories (book_id, category_id) VALUES (3, 3);
