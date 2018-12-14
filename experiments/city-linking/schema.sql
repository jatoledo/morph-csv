DROP TABLE IF EXISTS METROSTOPS;
CREATE TABLE METROSTOPS (
	stop_id VARCHAR(200), 
	stop_code INT, 
	stop_name VARCHAR(200),
	stop_desc VARCHAR(200),
	stop_lat DECIMAL, 
	stop_long DECIMAL, 
	zone_id VARCHAR(200),
	stop_url VARCHAR(200),
	location_type INT,
	parent_station VARCHAR(200),
	stop_timezone VARCHAR(200),
	weelchair_boarding INT,
	PRIMARY KEY (stop_id)
);

DROP TABLE IF EXISTS SCHOOLS;
CREATE TABLE SCHOOLS (
	pk INT,
	name VARCHAR (200),
	PRIMARY KEY (PK)
);

INSERT INTO METROSTOPS(stop_id, stop_code, stop_name, stop_desc, stop_lat, stop_long, 
	zone_id, stop_url, location_type, parent_station, stop_timezone, weelchair_boarding)
SELECT "stop_id", CONVERT("stop_code",INT), "stop_name", "stop_desc", CONVERT("stop_lat",DECIMAL), 
CONVERT("stop_long",DECIMAL), "zone_id", "stop_url", CONVERT("location_type",INT), "parent_station", 
"stop_timezone", CONVERT("weelchair_boarding",INT)
 	FROM CSVREAD( 'trainStops.csv', 'stop_id, stop_code,stop_name,stop_desc,stop_lat,stop_long,zone_id
  		stop_url,location_type,parent_station,weelchair_boarding', NULL );

INSERT INTO SCHOOLS (pk,name,transport)
SELECT CONVERT("pk", INT), "name"
	FROM CSVREAD("schools.csv",'pk,name',NULL);


ALTER TABLE METROSTOPS ADD transport VARCHAR(200);
ALTER TABLE SCHOOLS ADD transport VARCHAR(200);

UPDATE METROSTOPS SET transport = "REPLACE(LOWER(stop_name),' ','-')";
UPDATE SCHOOLS SET transport = "REPLACE(LOWER(SUBSTRING(transport,LOCATE('METRO:', transport)+7,LOCATE('BUS',transport)),' ','-')";

CREATE INDEX transportMetro ON METROSTOPS (transport);
CREATE INDEX transportSchool on SCHOOLS (transport);
