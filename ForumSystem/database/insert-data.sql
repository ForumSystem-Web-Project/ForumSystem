INSERT INTO forum.users (first_name, last_name, email, username, password, is_blocked, is_admin, has_phone_number)
VALUES
    ('Johnathan', 'Jefferson', 'johnjefferson@example.com', 'johnjeff123', 'securePass123!', false, true, 1),
    ('Alexandra', 'Smith', 'alexsmith@example.com', 'alexsmith42', 'Passw0rd@2023', false, true, 0),
    ('Benjamin', 'Brown', 'benbrown@example.com', 'benbrown21', 'Brownie$2023', false, false, 0),
    ('Catherine', 'White', 'cwhite@example.com', 'cathyW99', 'Cat#456789', false, false, 0),
    ('Michael', 'Taylor', 'michaelt@example.com', 'miketaylor7', 'MikePass123!', false, false, 0);

INSERT INTO forum.phone_numbers (phone_number, user_id)
VALUES
    ('+1234567890', 1),
    ('+9876543210', 2);

INSERT INTO forum.posts (user_id, title, content, timestamp)
VALUES
    (3, 'Why Learning SQL Is Important in 2025',
     'SQL remains one of the most crucial skills for data management and analysis...', '2025-01-29 16:22:00'),
    (4, 'Tips for Secure Password Management',
     'Using strong passwords is key to securing your accounts. Here are some tips...', '2025-01-29 16:22:00'),
    (5, 'Top 10 Places to Visit This Summer',
     'Are you planning your summer vacation? Here are the best places to consider...', '2025-01-29 16:22:00');

INSERT INTO forum.comments (post_id, user_id, content)
VALUES
    (1, 2, 'Great article! I found the examples really useful.'),
    (1, 3, 'I totally agree, SQL is essential. Thanks for sharing!'),
    (2, 4, 'Thanks for the tips! Password security is so important.'),
    (3, 5, 'This list is amazing! I’m adding these places to my bucket list.');

INSERT INTO forum.likes (user_id, post_id)
VALUES
    (1, 1), -- Johnathan Jefferson likes the first post
    (2, 1), -- Alexandra Smith likes the first post
    (3, 2), -- Benjamin Brown likes the second post
    (4, 2); -- Catherine White likes the second post