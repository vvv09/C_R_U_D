DROP TABLE IF EXISTS book;

CREATE TABLE book (
          id INT AUTO_INCREMENT  PRIMARY KEY,
          author VARCHAR(250) NOT NULL,
          title VARCHAR(250) NOT NULL,
          isbn VARCHAR(250) DEFAULT NULL
);

INSERT INTO book (author, title, isbn) VALUES
('Dorla Turcotte', 'Tirra Lirra by the River', 'ca3efb81-fae0-4623-bf08-ee82e5f59973'),
('Beatriz Schmitt PhD', 'Unweaving the Rainbow', '120b3e12-1728-40c8-a518-5f122e1a6fde'),
('Jeanelle Feeney', 'A Monstrous Regiment of Women', 'c49f47ff-a041-40c3-b7b0-228584a7afa7');
