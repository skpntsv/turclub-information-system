CREATE TABLE Tourist_type (
	id    SERIAL 		PRIMARY KEY,
	name  VARCHAR(100) 	NOT NULL UNIQUE
);

CREATE TABLE Specialization (
	id    SERIAL 		PRIMARY KEY,
	name  VARCHAR(100) 	NOT NULL UNIQUE
);

CREATE TABLE Hike_type (
  id    SERIAL 			PRIMARY KEY,
  name  VARCHAR(100) 	NOT NULL UNIQUE
);

CREATE TABLE Tourist (
	id        	SERIAL 			PRIMARY KEY,
	full_name 	VARCHAR(255) 	NOT NULL,
	gender    	VARCHAR(7) 		CHECK (type IN ('male', 'female')),
	birthday  	DATE     		CHECK ( birthday >= to_date('01.01.1900', 'DD-MM-YYYY')),
	email     	TEXT,
	phone     	VARCHAR(12),	
	category  	SMALLINT 		NOT NULL CHECK (category >= 1 AND category <= 10),
	type_id   	INTEGER 		NOT NULL REFERENCES Tourist_type(id)
);

CREATE TABLE Trainer (
	id                	INTEGER 	PRIMARY KEY  REFERENCES Tourist(id),
	salary            	MONEY,
	hire_date         	DATE,
	specialization_id 	INTEGER   	REFERENCES Specialization(id),
	section_id        	INTEGER 	NOT NULL REFERENCES Section(id)
);

CREATE TABLE SuperVisor (
	id          SERIAL 			PRIMARY KEY,
	full_name   VARCHAR(255) 	NOT NULL,
	salary      MONEY,
	hire_date   DATE,
	birthday    DATE,
	email       TEXT,
	phone       VARCHAR(12), 
);

CREATE TABLE Section (
	id            SERIAL PRIMARY KEY,
	name          VARCHAR(255) NOT NULL,
	supervisor_id INTEGER UNIQUE REFERENCES SuperVisor(id)
);

CREATE TABLE Group (
	id          SERIAL 			PRIMARY KEY,
	name        VARCHAR(255) 	NOT NULL,
	description VARCHAR(255),
	section_id  INTEGER 		REFERENCES Section(id),
	trainer_id  INTEGER 		REFERENCES Trainer(id)
);

CREATE TABLE Tourist_Groups (
	tourist_id  INTEGER 	REFERENCES Tourist(id),
	group_id    INTEGER 	REFERENCES Group(id),
	PRIMARY KEY (tourist_id, group_id)
);

CREATE TABLE Training (
	id          SERIAL 			PRIMARY KEY,
	plane_date  TIMESTAMP,
	real_date   TIMESTAMP,
	place       TEXT,
	duration    INTERVAL,
	trainer_id  INTEGER 		REFERENCES Trainer(id),
	group_id    INTEGER 		REFERENCES Group(id),
	section_id  INTEGER 		REFERENCES Section(id)
);

CREATE TABLE Attendance (
	tourist_id  INTEGER 	REFERENCES Tourist(id),
	training_id INTEGER 	REFERENCES Training(id),
	PRIMARY KEY (tourist_id, training_id)
);

CREATE TABLE Route (
	id                  SERIAL 			PRIMARY KEY,
	name                VARCHAR(255) 	NOT NULL,
	length_meters       INTEGER,
	duration_hours      INTERVAL,
	difficulty_category SMALLINT 		NOT NULL CHECK (category >= 1 AND category <= 10),
	description         TEXT
);

CREATE TABLE Hike (
	id              SERIAL 			PRIMARY KEY,
	plan_start_date TIMESTAMPTZ 	NOT NULL,
	real_start_date TIMESTAMPTZ,
	real_end_date   TIMESTAMPTZ,
	is_planned      BOOLEAN,
	hike_type_id    INTEGER 		REFERENCES Hike_type(id),
	instructor_id   INTEGER 		REFERENCES Tourist(id),
	route_id        INTEGER 		REFERENCES Route(id)
);

CREATE TABLE Diary (
	id      SERIAL 			PRIMARY KEY,
	date    TIMESTAMPTZ,
	content TEXT,
	hike_id INTEGER 		REFERENCES Hike(id)
);

CREATE TABLE Checkpoint (
	id          SERIAL 			PRIMARY KEY,
	description TEXT,
	type        VARCHAR(7) 		CHECK (type IN ('passing', 'rest')),
	latitude    NUMERIC(3, 7) 	CHECK (latitude >= -90 AND latitude <= 90),
	longitude   NUMERIC(4, 7) 	CHECK (longitude >= -180 AND longitude <= 180)
);

CREATE TABLE Route_Checkpoints (
	id            SERIAL 	UNIQUE,
	point_number  SMALLINT,
	route_id      INTEGER 	REFERENCES Route(id),
	checkpoint_id INTEGER 	REFERENCES Checkpoint(id),
	PRIMARY KEY (point_number, route_id, checkpoint_id)
);

CREATE TABLE Sсhedule_hike_checkpoints (
	plan_date             TIMESTAMPTZ 		NOT NULL,
	real_date             TIMESTAMPTZ,
	hike_id               INTEGER 			REFERENCES Hike(id),
	route_checkpoints_id  INTEGER 			REFERENCES Route_Checkpoints(id),
	PRIMARY KEY (hike_id, route_checkpoints_id)
);

CREATE TABLE Hike_Tourists (
	tourist_id  INTEGER 	REFERENCES Tourist(id),
	hike_id     INTEGER 	REFERENCES Hike(id),
	PRIMARY KEY (tourist_id, hike_id)
);

CREATE TABLE Competition (
	id          SERIAL 			PRIMARY KEY,
	date        TIMESTAMPTZ		NOT NULL,
	name        VARCHAR(255) 	NOT NULL,
	place       VARCHAR(255)	NOT NULL,
	description TEXT
);

CREATE TABLE Tourist_Competitions (
	tourist_id      INTEGER 	REFERENCES Tourist(id),
	competition_id  INTEGER 	REFERENCES Competition(id),
	PRIMARY KEY (tourist_id, competition_id)
);
