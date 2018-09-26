CREATE TYPE questionType AS ENUM ('SCALA', 'TEXT', 'TEXT_LONG');
CREATE TYPE locale AS ENUM ('de', 'en');

CREATE TABLE contractor
(
  ID SERIAL NOT NULL,
  NAME TEXT NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE client
(
  username TEXT NOT NULL,
  PRIMARY KEY (username)
);

CREATE TABLE accessControl
(
  username TEXT NOT NULL,
  timestamp TIMESTAMP NOT NULL,
  contractorID INT NOT NULL,
  FOREIGN KEY (contractorID) REFERENCES contractor(ID) ,
  FOREIGN KEY (username) REFERENCES client(username),
  PRIMARY KEY (username,contractorID)
);

CREATE TABLE questions
(
  ID SERIAL NOT NULL,
  questionType questionType NOT NULL,
  latestQuestion BOOLEAN NOT NULL,
  PRIMARY KEY (ID)
);

CREATE TABLE questions_tr
(
  questionID int NOT NULL,
  question TEXT NOT NULL,
  locale locale NOT NULL,
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
  FOREIGN KEY (username) REFERENCES Client(username) ,
  FOREIGN KEY (contractorID) REFERENCES contractor(ID),
  FOREIGN KEY (questionID) REFERENCES questions(ID)
);

INSERT INTO contractor(ID,NAME) VALUES (1,'Abteilung Genderkompetenz');
INSERT INTO contractor(ID,NAME) VALUES (2,'Bibliothek');
INSERT INTO contractor(ID,NAME) VALUES (3,'Büro für Öffentlichkeitsarbeit');
INSERT INTO contractor(ID,NAME) VALUES (4,'Continuing Education Center');
INSERT INTO contractor(ID,NAME) VALUES (5,'Datenschutz und Dokumentenmanagement');
INSERT INTO contractor(ID,NAME) VALUES (6,'Department für Finanzen');
INSERT INTO contractor(ID,NAME) VALUES (7,'EU-Forschungssupport');
INSERT INTO contractor(ID,NAME) VALUES (8,'Forschungsmarketing');
INSERT INTO contractor(ID,NAME) VALUES (9,'Forschungs- und Transfersupport');
INSERT INTO contractor(ID,NAME) VALUES (10,'Fundraising & Sponsoring');
INSERT INTO contractor(ID,NAME) VALUES (11,'Gebäude und Technik');
INSERT INTO contractor(ID,NAME) VALUES (12,'Interne Revision');
INSERT INTO contractor(ID,NAME) VALUES (13,'International Office');
INSERT INTO contractor(ID,NAME) VALUES (14,'Personaladministration');
INSERT INTO contractor(ID,NAME) VALUES (15,'Personalentwicklung und Betriebliche Gesundheitsförderung');
INSERT INTO contractor(ID,NAME) VALUES (16,'Studienabteilung');
INSERT INTO contractor(ID,NAME) VALUES (17,'Teaching Support Center (TSC)');
INSERT INTO contractor(ID,NAME) VALUES (18,'Tieftemperaturanlagen');
INSERT INTO contractor(ID,NAME) VALUES (19,'TU.it (IT Solutions)');
INSERT INTO contractor(ID,NAME) VALUES (20,'Universitätsarchiv');
INSERT INTO contractor(ID,NAME) VALUES (21,'Universitätsentwicklung und Qualitätsmanagement');