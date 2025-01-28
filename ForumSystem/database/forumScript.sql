CREATE TABLE users (
                       user_id INT AUTO_INCREMENT PRIMARY KEY,
                       first_name VARCHAR(32) NOT NULL CHECK (LENGTH(first_name) BETWEEN 4 AND 32),
                       last_name VARCHAR(32) NOT NULL CHECK (LENGTH(last_name) BETWEEN 4 AND 32),
                       email VARCHAR(255) NOT NULL UNIQUE,
                       username VARCHAR(32) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL
);

CREATE TABLE admins (
                        admin_id INT PRIMARY KEY,
                        user_id INT NOT NULL UNIQUE,
                        phone_number VARCHAR(15) DEFAULT '' NOT NULL,
                        FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE posts (
                       post_id INT AUTO_INCREMENT PRIMARY KEY,
                       user_id INT NOT NULL,
                       title VARCHAR(64) NOT NULL CHECK (LENGTH(title) BETWEEN 16 AND 64),
                       content TEXT NOT NULL CHECK (LENGTH(content) BETWEEN 32 AND 8192),
                       FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE comments (
                          comment_id INT AUTO_INCREMENT PRIMARY KEY,
                          post_id INT NOT NULL,
                          user_id INT NOT NULL,
                          content TEXT NOT NULL CHECK (LENGTH(content) > 0 AND LENGTH(content) <= 8192),
                          FOREIGN KEY (post_id) REFERENCES posts(post_id),
                          FOREIGN KEY (user_id) REFERENCES users(user_id)
                      );

CREATE TABLE likes (
                       like_id INT PRIMARY KEY AUTO_INCREMENT,
                       user_id INT NOT NULL,
                       post_id INT NOT NULL,
                       FOREIGN KEY (user_id) REFERENCES users(user_id),
                       FOREIGN KEY (post_id) REFERENCES posts(post_id),
                       UNIQUE(user_id, post_id)
);