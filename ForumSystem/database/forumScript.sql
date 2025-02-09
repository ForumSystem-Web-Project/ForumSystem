CREATE TABLE users (
                       user_id INT AUTO_INCREMENT PRIMARY KEY,
                       first_name VARCHAR(32) NOT NULL CHECK (LENGTH(first_name) BETWEEN 4 AND 32),
                       last_name VARCHAR(32) NOT NULL CHECK (LENGTH(last_name) BETWEEN 4 AND 32),
                       email VARCHAR(255) NOT NULL UNIQUE,
                       username VARCHAR(32) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       is_admin BOOLEAN NOT NULL,
                       is_blocked BOOLEAN NOT NULL
);

CREATE TABLE  phone_numbers
(
                        phone_number_id INT AUTO_INCREMENT PRIMARY KEY ,
                        phone_number VARCHAR(32) NOT NULL ,
                        user_id INT NOT NULL,
                        CONSTRAINT phone_numbers_users_user_id_fk
                            FOREIGN KEY (user_id) REFERENCES users (user_id)
                                ON DELETE CASCADE
);

CREATE TABLE posts (
                       post_id INT AUTO_INCREMENT PRIMARY KEY,
                       user_id INT NOT NULL,
                       title VARCHAR(64) NOT NULL CHECK (LENGTH(title) BETWEEN 16 AND 64),
                       content TEXT NOT NULL CHECK (LENGTH(content) BETWEEN 32 AND 8192),
                       createdAt TIMESTAMP DEFAULT current_timestamp,
                       CONSTRAINT posts_users_user_id_fk
                           FOREIGN KEY (user_id) REFERENCES users (user_id)
                               ON DELETE CASCADE
);

CREATE TABLE comments (
                          comment_id INT AUTO_INCREMENT PRIMARY KEY,
                          post_id INT NOT NULL,
                          user_id INT NOT NULL,
                          content TEXT NOT NULL CHECK (LENGTH(content) > 0 AND LENGTH(content) <= 8192),
                          CONSTRAINT comments_posts_post_id_fk
                              FOREIGN KEY (post_id) REFERENCES posts (post_id)
                                  ON DELETE CASCADE,
                          CONSTRAINT comments_users_user_id_fk
                              FOREIGN KEY (user_id) REFERENCES users (user_id)
                                  ON DELETE CASCADE
                      );

CREATE TABLE likes (
                       like_id INT PRIMARY KEY AUTO_INCREMENT,
                       user_id INT NOT NULL,
                       post_id INT NOT NULL,
                       CONSTRAINT likes_posts_post_id_fk
                           FOREIGN KEY (post_id) REFERENCES posts (post_id)
                               ON DELETE CASCADE,
                       CONSTRAINT likes_users_user_id_fk
                           FOREIGN KEY (user_id) REFERENCES users (user_id)
                               ON DELETE CASCADE
);