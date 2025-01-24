INSERT INTO Users (FirstName, LastName, Email, Username, Password)
VALUES
    ('Johnathan', 'Jefferson', 'johnjefferson@example.com', 'johnjeff123', 'securePass123!'),
    ('Alexandra', 'Smith', 'alexsmith@example.com', 'alexsmith42', 'Passw0rd@2023'),
    ('Benjamin', 'Brown', 'benbrown@example.com', 'benbrown21', 'Brownie$2023'),
    ('Catherine', 'White', 'cwhite@example.com', 'cathyW99', 'Cat#456789'),
    ('Michael', 'Taylor', 'michaelt@example.com', 'miketaylor7', 'MikePass123!');

INSERT INTO Admins (AdminID, UserID, PhoneNumber)
VALUES
    (1, 1, '+1234567890'),
    (2, 2, '+9876543210');

INSERT INTO Posts (UserID, Title, Content, Likes)
VALUES
    (3, 'Why Learning SQL Is Important in 2025',
     'SQL remains one of the most crucial skills for data management and analysis...', 15),
    (4, 'Tips for Secure Password Management',
     'Using strong passwords is key to securing your accounts. Here are some tips...', 25),
    (5, 'Top 10 Places to Visit This Summer',
     'Are you planning your summer vacation? Here are the best places to consider...', 30);

INSERT INTO Comments (PostID, UserID, Content)
VALUES
    (1, 2, 'Great article! I found the examples really useful.'),
    (1, 3, 'I totally agree, SQL is essential. Thanks for sharing!'),
    (2, 4, 'Thanks for the tips! Password security is so important.'),
    (3, 5, 'This list is amazing! I’m adding these places to my bucket list.');
