DROP TABLE IF EXISTS PEOPLE;
CREATE TABLE PEOPLE (
	name VARCHAR(200), 
	ln1 VARCHAR (200), 
	ln2 VARCHAR(200), 
	cName VARCHAR(200), 
	nCourses INT DEFAULT (0), 
	PRIMARY KEY (name), 
	FOREIGN KEY (cName) REFERENCES Countries(cName));

DROP TABLE IF EXISTS COMMENTS;
CREATE TABLE COMMENTS (
	comment VARCHAR(200), 
	creationDate DATE, 
	username VARCHAR(200), 
	modifiedDate INT, 
	PRIMARY KEY (comment,creationDate,username), 
	FOREIGN KEY (modifiedDate) REFERENCES dateComments(id));

DROP TABLE IF EXISTS MODIFIEDDATES;
CREATE TABLE MODIFIEDDATES (
	id INT, 
	modifiedDate DATE, 
	PRIMARY KEY (id));

DROP TABLE IF EXISTS COUNTRIES;
CREATE TABLE COUNTRIES (
	cName VARCHAR(200), 
	cOfficialName VARCHAR(200), 
	PRIMARY KEY (cName));
 
INSERT INTO people(name,ln1,ln2,cName,nCourses)  
SELECT "name","ln1","ln2","cName",CONVERT( "nCoruses", INT)
  FROM CSVREAD( 'people.csv', 'name,ln1,ln2,cName,nCourses', NULL );

INSERT INTO comments(comment,creationDate,username,modifiedDate)  
SELECT "comment",CONVERT("creationDate",DATE),"username",CONVERT("modifiedDate",INT)
  FROM CSVREAD( 'comments.csv', 'comment,creationDate,username,modifiedDate', NULL );

INSERT INTO dateComments(id,modifiedDate)  
SELECT "id","modifiedDate"
  FROM CSVREAD( 'dateComments.csv', 'id,modifiedDate', NULL );

INSERT INTO countries(cName,cOfficialName)  
SELECT "cName","cOfficialName"
  FROM CSVREAD( 'people.csv', 'cName,cOfficialName', NULL );

ALTER TABLE people ADD email VARCHAR(200);
ALTER TABLE people ADD author VARCHAR(200);

UPDATE people SET email = "SUBSTRING(name,1,1) || ln1 || '@fi.upm.es'))";
UPDATE people SET author = "SUBSTRING(name,1,1) || ln1";

CREATE INDEX authors ON people (author);
CREATE INDEX usernames ON comments (username);
