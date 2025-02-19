INSERT INTO forum.users (first_name, last_name, email, username, password, is_blocked, is_admin)
VALUES
    ('Johnathan', 'Jefferson', 'johnjefferson@example.com', 'johnjeff123', 'securePass123!', false, true),
    ('Alexandra', 'Smith', 'alexsmith@example.com', 'alexsmith42', 'Passw0rd@2023', false, true),
    ('Benjamin', 'Brown', 'benbrown@example.com', 'benbrown21', 'Brownie$2023', false, false),
    ('Catherine', 'White', 'cwhite@example.com', 'cathyW99', 'Cat#456789', false, false),
    ('Michael', 'Taylor', 'michaelt@example.com', 'miketaylor7', 'MikePass123!', false, false),
    ('Emma', 'Johnson', 'emma.johnson@example.com', 'emmaJ88', 'EmmaSecure!45', false, false),
    ('Daniel', 'Martinez', 'danmart@example.com', 'danM23', 'MartinezPass!90', false, false),
    ('Sophia', 'Harris', 'sophia.h@example.com', 'sophiaH12', 'Sophia#7890', false, false),
    ('Liam', 'Anderson', 'liam.anderson@example.com', 'liamA77', 'Liam&Secure99', false, false),
    ('Olivia', 'Walker', 'olivia.walker@example.com', 'oliviaW22', 'Walker@Strong12', false, false);


INSERT INTO forum.phone_numbers (phone_number, user_id)
VALUES
    ('+1234567890', 1),
    ('+9876543210', 2);

INSERT INTO forum.posts (user_id, title, content, createdAt)
VALUES
    (3, 'Why Learning SQL Is Important in 2025',
     'SQL remains one of the most crucial skills for data management and analysis...', '2025-01-29 16:22:00'),
    (4, 'Tips for Secure Password Management',
     'Using strong passwords is key to securing your accounts. Here are some tips...', '2025-01-29 16:22:00'),
    (5, 'Top 10 Places to Visit This Summer',
     'Are you planning your summer vacation? Here are the best places to consider...', '2025-01-29 16:22:00'),
    (9, 'The Future of Artificial Intelligence in 2025',
     'AI continues to revolutionize industries. Here’s what to expect in 2025...', '2025-01-30 10:15:00'),
    (8, 'How to Improve Your Coding Skills',
     'Mastering coding takes practice. Here are some proven strategies...', '2025-01-30 12:45:00'),
    (7, 'The Benefits of a Healthy Morning Routine',
     'Starting your day right can improve productivity and well-being...', '2025-01-30 08:00:00'),
    (6, 'Essential Cybersecurity Practices for Beginners',
     'Protecting your digital presence is crucial. Follow these basic steps...', '2025-01-30 14:30:00'),
    (10, 'Understanding Blockchain Technology',
     'Blockchain is more than just cryptocurrency. Here’s how it works...', '2025-01-30 16:00:00'),
    (4, 'The Best Programming Languages to Learn in 2025',
     'Wondering which languages will dominate the industry? Check this list...', '2025-01-30 18:20:00');

INSERT INTO forum.comments (post_id, user_id, content)
VALUES
    (1, 2, 'Great article! I found the examples really useful.'),
    (1, 3, 'I totally agree, SQL is essential. Thanks for sharing!'),
    (2, 4, 'Thanks for the tips! Password security is so important.'),
    (3, 5, 'This list is amazing! I’m adding these places to my bucket list.'),
    (9, 6, 'Very informative! I learned a lot about AI trends.'),
    (2, 7, 'I struggle with coding sometimes, but these tips are helpful!'),
    (3, 8, 'Morning routines really make a difference. Thanks for sharing!'),
    (4, 9, 'Cybersecurity should be taught in schools. Great article!'),
    (5, 10, 'Blockchain is the future! Excited to see where it goes.'),
    (6, 10, 'I’m starting with Python, glad to see it on the list!'),
    (1, 5, 'This article explains AI so well! Looking forward to more.'),
    (2, 7, 'I just started learning SQL, and this was really helpful.'),
    (3, 9, 'I’ve been using weak passwords... time to change that!'),
    (4, 3, 'I visited some of these places last year. Highly recommend!'),
    (5, 6, 'Thanks for simplifying SQL concepts. Great read!'),
    (6, 1, 'A strong morning routine has improved my productivity!'),
    (1, 4, 'AI is evolving so fast! It’s both exciting and scary.'),
    (2, 1, 'I always forget SQL joins. This article made it clearer.'),
    (3, 6, 'Password managers are lifesavers. Thanks for the guide!'),
    (4, 8, 'Just booked my tickets! Can’t wait for this summer trip.');


INSERT INTO forum.likes (user_id, post_id)
VALUES
    (1, 1),
    (2, 1),
    (3, 2),
    (4, 2);