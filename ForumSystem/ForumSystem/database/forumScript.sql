CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL CHECK (CHAR_LENGTH(first_name) BETWEEN 4 AND 32),
    last_name VARCHAR(50) NOT NULL CHECK (CHAR_LENGTH(last_name) BETWEEN 4 AND 32),
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE CHECK (email REGEXP '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$'),
    phone_number VARCHAR(15),
    is_admin TINYINT(1) DEFAULT 0
);

CREATE TABLE posts (
    post_id INT AUTO_INCREMENT PRIMARY KEY,
    created_by INT NOT NULL,
    title VARCHAR(64) NOT NULL CHECK (CHAR_LENGTH(title) BETWEEN 16 AND 64),
    content TEXT NOT NULL CHECK (CHAR_LENGTH(content) BETWEEN 32 AND 8192),
    likes INT DEFAULT 0,
    FOREIGN KEY (created_by) REFERENCES users(user_id)
);

CREATE TABLE comments (
    comment_id INT AUTO_INCREMENT PRIMARY KEY,
    post_id INT NOT NULL,
    created_by INT NOT NULL,
    content TEXT NOT NULL CHECK (CHAR_LENGTH(content) >= 1),
    FOREIGN KEY (post_id) REFERENCES posts(post_id),
    FOREIGN KEY (created_by) REFERENCES users(user_id)
);
