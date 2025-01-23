INSERT INTO users (username, password, first_name, last_name, email, phone_number, is_admin)
VALUES
    ('johndoe', 'password123', 'John', 'Darvin', 'john.doe@example.com', '123-456-7890', 0),
    ('janedoe', 'securepass', 'Jane', 'Angelov', 'jane.doe@example.com', NULL, 0),
    ('admin1', 'adminpass', 'Alice', 'Admin', 'admin1@example.com', '555-555-5555', 1),
    ('user123', 'pass1234', 'Mark', 'Smith', 'mark.smith@example.com', NULL, 0),
    ('sarah22', 'mypassword', 'Sarah', 'Connor', 'sarah.connor@example.com', '987-654-3210', 0),
    ('beerlover', 'beer4life', 'Mike', 'Johnson', 'mike.johnson@example.com', NULL, 0),
    ('admin2', 'anotheradmin', 'Bobi', 'Manager', 'bob.manager@example.com', '333-333-3333', 1),
    ('karen56', 'bestuser', 'Karen', 'Jones', 'karen.jones@example.com', NULL, 0),
    ('techguy', 'techy123', 'Chris', 'Evans', 'chris.evans@example.com', NULL, 0),
    ('linda44', 'linda1234', 'Linda', 'Taylor', 'linda.taylor@example.com', NULL, 0);

INSERT INTO posts (created_by, title, content, likes)
VALUES
    (1, 'How to Brew Beer', 'This is a detailed guide on brewing beer...', 15),
    (2, 'Craft Beer Reviews', 'Let’s discuss the top craft beers...', 20),
    (3, 'Admin Post', 'Here’s an update from the admin...', 30),
    (4, 'Tech Tips', 'Some great tips for handling tech issues...', 18),
    (5, 'Cooking with Beer', 'Creative ways to use beer in cooking...', 25),
    (6, 'Best Beer Spots', 'A guide to the best places to drink beer...', 10),
    (7, 'Admin Announcement', 'Important updates for all users...', 50),
    (8, 'User Feedback', 'Please share your feedback on the platform...', 5),
    (9, 'Travel and Beer', 'Combining travel with beer experiences...', 12),
    (10, 'Fitness and Beer?', 'How to balance fitness with enjoying beer...', 8);

INSERT INTO comments (post_id, created_by, content)
VALUES
    (1, 2, 'Great post! I love brewing beer too.'),
    (1, 3, 'As an admin, I appreciate this content.'),
    (2, 1, 'Thanks for sharing your reviews.'),
    (3, 2, 'Looking forward to more updates!'),
    (4, 5, 'These tech tips are very helpful, thank you!'),
    (5, 6, 'Wow, I never thought of cooking with beer before.'),
    (6, 1, 'I’ll definitely visit these beer spots soon!'),
    (7, 4, 'Thank you for the announcement, admin.'),
    (8, 9, 'Here’s my feedback: the platform is great but needs improvements.'),
    (9, 10, 'I love traveling and beer, so this post is perfect!'),
    (10, 8, 'Interesting perspective on balancing fitness and beer.'),
    (1, 4, 'Can you share some brewing tips for beginners?'),
    (2, 6, 'What’s your favorite craft beer?');

