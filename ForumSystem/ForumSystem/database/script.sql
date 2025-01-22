CREATE TABLE Users (
                       UserID INT AUTO_INCREMENT PRIMARY KEY,
                       FirstName VARCHAR(32) NOT NULL CHECK (LENGTH(FirstName) BETWEEN 4 AND 32),
                       LastName VARCHAR(32) NOT NULL CHECK (LENGTH(LastName) BETWEEN 4 AND 32),
                       Email VARCHAR(255) NOT NULL UNIQUE,
                       Username VARCHAR(32) NOT NULL UNIQUE,
                       Password VARCHAR(255) NOT NULL
);

CREATE TABLE Admins (
                        AdminID INT PRIMARY KEY,
                        UserID INT NOT NULL UNIQUE,
                        PhoneNumber VARCHAR(15) NULL,
                        FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

CREATE TABLE Posts (
                       PostID INT AUTO_INCREMENT PRIMARY KEY,
                       UserID INT NOT NULL,
                       Title VARCHAR(64) NOT NULL CHECK (LENGTH(Title) BETWEEN 16 AND 64),
                       Content TEXT NOT NULL CHECK (LENGTH(Content) BETWEEN 32 AND 8192),
                       Likes INT DEFAULT 0 CHECK (Likes >= 0),
                       CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       FOREIGN KEY (UserID) REFERENCES Users(UserID)
);

CREATE TABLE Comments (
                          CommentID INT AUTO_INCREMENT PRIMARY KEY,
                          PostID INT NOT NULL,
                          UserID INT NOT NULL,
                          Content TEXT NOT NULL CHECK (LENGTH(Content) > 0 AND LENGTH(Content) <= 8192),
                          CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (PostID) REFERENCES Posts(PostID),
                          FOREIGN KEY (UserID) REFERENCES Users(UserID)
);
