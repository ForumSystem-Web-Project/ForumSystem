INSERT INTO Users (FirstName, LastName, Email, Username, Password) VALUES
                                                                       ('Alice', 'Smith', 'alice.smith@email.com', 'alice_smith', 'password123'),
                                                                       ('Bobi', 'Johnson', 'bob.johnson@email.com', 'bob_johnson', 'password123'),
                                                                       ('Charlie', 'Brown', 'charlie.brown@email.com', 'charlie_brown', 'password123'),
                                                                       ('David', 'Williams', 'david.williams@email.com', 'david_williams', 'password123'),
                                                                       ('Evelin', 'Jones', 'eva.jones@email.com', 'eva_jones', 'password123'),
                                                                       ('Ivan', 'Miller', 'fay.miller@email.com', 'fay_miller', 'password123'),
                                                                       ('George', 'Davis', 'george.davis@email.com', 'george_davis', 'password123'),
                                                                       ('Helen', 'Martinez', 'helen.martinez@email.com', 'helen_martinez', 'password123'),
                                                                       ('Isaac', 'Garcia', 'isaac.garcia@email.com', 'isaac_garcia', 'password123'),
                                                                       ('Jill', 'Rodriguez', 'jill.rodriguez@email.com', 'jill_rodriguez', 'password123');

INSERT INTO Admins (AdminID, UserID, PhoneNumber) VALUES
                                                      (1, 1, '555-1234'),
                                                      (2, 3, '555-5678'),
                                                      (3, 5, '555-8765'),
                                                      (4, 7, '555-2345'),
                                                      (5, 9, '555-6789');

INSERT INTO Posts (UserID, Title, Content) VALUES
                                               (1, 'First Post by Alice', 'This is Alice\'s first post content. Lorem ipsum dolor sit amet, consectetur adipiscing elit.'),
                                               (2, 'Bob\'s Post about him and his journey', 'This is Bob\'s content. Lorem ipsum dolor sit amet.'),
                                               (3, 'Charlie\'s Deep Thoughts', 'Charlie here, this is my post. I am writing some content about my thoughts on technology.'),
                                               (1, 'Alice\'s Second Post in the forum', 'Alice is back with another post. This time, let\'s talk about AI advancements.'),
                                               (4, 'David\'s First Post in the forum', 'David here. This post is about the wonders of space exploration.'),
                                               (5, 'Eva’s Post about the enviroment', 'Eva writes about environmental changes and how we can help save the planet.'),
                                               (6, 'Fay’s First Post about the feature', 'Fay shares thoughts on the future of education in the digital age.'),
                                               (2, 'Bob’s Thoughts and Opinions', 'Bob shares his opinion on the importance of personal growth and self-care.'),
                                               (7, 'George’s Adventure across Europe', 'George talks about his recent adventure across Europe.'),
                                               (8, 'Helen’s Creative Ideas for Productivity', 'Helen discusses creative ways to improve your productivity at work.'),
                                               (3, 'Charlie’s Second Post of Social Media', 'Charlie writes about the impact of social media on relationships.'),
                                               (9, 'Isaac’s Post about renewable energy', 'Isaac explores the potential of renewable energy sources and their impact on our future.'),
                                               (10, 'Jill’s Insight about global economy', 'Jill provides insights into the current state of the global economy.'),
                                               (6, 'Fay’s Second Post in the Forum', 'Fay’s second post is about the power of books and their impact on personal growth.'),
                                               (7, 'George’s Reflections about his travel', 'George reflects on his past travels and the lessons learned.');

INSERT INTO Comments (PostID, UserID, Content) VALUES
                                                   (32, 2, 'Great post Alice, I totally agree with your points!'),
                                                   (33, 3, 'Interesting thoughts, Bob! I believe self-care is vital.'),
                                                   (34, 1, 'Charlie, I think technology is evolving at an incredible rate!'),
                                                   (35, 4, 'I enjoyed reading this Alice. AI is indeed advancing rapidly.'),
                                                   (36, 5, 'David, your post on space exploration was inspiring!'),
                                                   (37, 6, 'I love the ideas you shared about environmental changes, Eva.'),
                                                   (38, 2, 'Fay, I agree that digital education has a bright future.'),
                                                   (31, 1, 'Bob, your ideas on self-care resonate with me.'),
                                                   (39, 7, 'George, your adventure across Europe sounds incredible!'),
                                                   (40, 8, 'Helen, your tips on productivity are very useful.'),
                                                   (41, 3, 'Charlie, social media’s impact on relationships is undeniable.'),
                                                   (42, 9, 'Isaac, I appreciate your perspective on renewable energy.'),
                                                   (43, 10, 'Jill, your thoughts on the global economy were enlightening.'),
                                                   (44, 6, 'Fay, books are indeed powerful in shaping our minds.'),
                                                   (45, 7, 'George, I love hearing about your reflections on travel.');

