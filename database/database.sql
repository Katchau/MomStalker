.mode columns
.headers on
.nullvalue NULL

PRAGMA foreign_key = on;

/* USERS */
DROP TABLE IF EXISTS Users;
CREATE TABLE Users
(
	id INTEGER PRIMARY KEY,
	username VARCHAR(16) UNIQUE NOT NULL,
	password VARCHAR(32) NOT NULL,
	xCoord REAL,
	yCoord REAL
    
);

DROP TABLE IF EXISTS Amizade;
CREATE TABLE Amizade
(
	user1 INTEGER,
	user2 INTEGER,
	PRIMARY KEY (user1, user2),
	
	CONSTRAINT User1 FOREIGN KEY (user1)
		REFERENCES Users(id),
	CONSTRAINT User2 FOREIGN KEY (user2)
		REFERENCES Users(id),
	
	CHECK (user1 < user2)
);

DROP TABLE IF EXISTS FriendRequest;
CREATE TABLE FriendRequest
(
	user1 INTEGER,
	user2 INTEGER,
	PRIMARY KEY (user1, user2),
	
	CONSTRAINT User1 FOREIGN KEY (user1)
		REFERENCES Users(id),
	CONSTRAINT User2 FOREIGN KEY (user2)
		REFERENCES Users(id),
	
	CHECK (user1 < user2)
);

DROP TABLE IF EXISTS Event;
CREATE TABLE Event
(
	id INTEGER PRIMARY KEY,
	uHost INTEGER NOT NULL,
	name VARCHAR(32) NOT NULL,
	xCoord REAL NOT NULL,
	yCoord REAL NOT NULL,
	
	CONSTRAINT UserHost FOREIGN KEY (uHost)
		REFERENCES Users(id)
);