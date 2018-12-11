DROP TABLE IF EXISTS TRAINSTOPS;
CREATE TABLE TRAINSTOPS (
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

DROP TABLE IF EXISTS TRAMSTOPS;
CREATE TABLE TRAMSTOPS (
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

INSERT INTO TRAINSTOPS(stop_id, stop_code, stop_name, stop_desc, stop_lat, stop_long, 
	zone_id, stop_url, location_type, parent_station, stop_timezone, weelchair_boarding)
SELECT "stop_id", CONVERT("stop_code",INT), "stop_name", "stop_desc", CONVERT("stop_lat",DECIMAL), 
CONVERT("stop_long",DECIMAL), "zone_id", "stop_url", CONVERT("location_type",INT), "parent_station", 
"stop_timezone", CONVERT("weelchair_boarding",INT)
  FROM CSVREAD( 'trainStops.csv', 'stop_id, stop_code,stop_name,stop_desc,stop_lat,stop_long,zone_id
  	stop_url,location_type,parent_station,weelchair_boarding', NULL );

INSERT INTO TRAMSTOPS(stop_id, stop_code, stop_name, stop_desc, stop_lat, stop_long, 
	zone_id, stop_url, location_type, parent_station, stop_timezone, weelchair_boarding)
SELECT "stop_id", CONVERT("stop_code",INT), "stop_name", "stop_desc", CONVERT("stop_lat",DECIMAL), 
CONVERT("stop_long",DECIMAL), "zone_id", "stop_url", CONVERT("location_type",INT), "parent_station", 
"stop_timezone", CONVERT("weelchair_boarding",INT)
  FROM CSVREAD( 'trainStops.csv', 'stop_id, stop_code,stop_name,stop_desc,stop_lat,stop_long,zone_id
  	stop_url,location_type,parent_station,weelchair_boarding', NULL );


ALTER TABLE TRAINSTOPS ADD sameAs VARCHAR(200);
ALTER TABLE TRAMSTOPS ADD sameAs VARCHAR(200);

UPDATE TRAINSTOPS SET sameAS = "REPLACE(LOWER(stop_name),' ','-')";
UPDATE TRAMSTOPS SET sameAS = "REPLACE(LOWER(stop_name),' ','-')";

CREATE INDEX trainNames ON TRAINSTOPS (sameAs);
CREATE INDEX tramNames ON TRAMSTOPS (sameAs);