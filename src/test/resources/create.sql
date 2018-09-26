CREATE TABLE contractors
(
  ID SERIAL NOT NULL,
  NAME TEXT NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE clients
(
  username TEXT NOT NULL,
  PRIMARY KEY (username)
);

CREATE TABLE accessControl
(
  username TEXT NOT NULL,
  timestamp TIMESTAMP NOT NULL,
  contractorID INT NOT NULL,
  FOREIGN KEY (contractorID) REFERENCES contractors(ID) ,
  FOREIGN KEY (username) REFERENCES clients(username),
  PRIMARY KEY (username,contractorID)
);

CREATE TABLE questions
(
  ID SERIAL NOT NULL,
  type VARCHAR(255) NOT NULL,
  latestQuestion BOOLEAN NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE questions_tr
(
  questionID int NOT NULL,
  question TEXT NOT NULL,
  locale VARCHAR(255) NOT NULL,
  FOREIGN KEY (questionID) REFERENCES questions(ID)
);

CREATE TABLE answers
(
  ID SERIAL NOT NULL,
  questionID INT NOT NULL,
  text TEXT NOT NULL,
  contractorID INT NOT NULL,
  username TEXT NOT NULL,
  submissiontime TIMESTAMP NOT NULL,
  PRIMARY KEY (ID),
  FOREIGN KEY (username) REFERENCES Clients(username) ,
  FOREIGN KEY (contractorID) REFERENCES contractors(ID),
  FOREIGN KEY (questionID) REFERENCES questions(ID)
);