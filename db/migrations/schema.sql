CREATE TABLE Tourist_type (
	id    SERIAL 		PRIMARY KEY,
	name  VARCHAR(100) 	NOT NULL UNIQUE
);

CREATE TABLE Specialization (
	id    SERIAL 		PRIMARY KEY,
	name  VARCHAR(100) 	NOT NULL UNIQUE
);

CREATE TABLE Hike_type (
	id    SERIAL 		PRIMARY KEY,
	name  VARCHAR(100) 	NOT NULL UNIQUE
);

CREATE TABLE Contacts (
	id 				SERIAL
	email 			VARCHAR(256) 	NOT NULL CHECK (email ~* '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$'),
	main_phone 		VARCHAR(15) 	NOT NULL CHECK (main_phone ~* '^(\+7|8)-\d{3}-\d{3}-\d{2}-\d{2}$'),
	reserve_phone	VARCHAR(15) 	CHECK (reserve_phone ~* '^(\+7|8)-\d{3}-\d{3}-\d{2}-\d{2}$'),
	emergency_phone VARCHAR(15) 	CHECK (emergency_phone ~* '^(\+7|8)-\d{3}-\d{3}-\d{2}-\d{2}$')
);

CREATE TABLE Tourist (
	id        	SERIAL 			PRIMARY KEY,
	full_name 	VARCHAR(255) 	NOT NULL,
	gender    	VARCHAR(7) 		NOT NULL CHECK (gender IN ('male', 'female')),
	birthday  	DATE     		NOT NULL CHECK (birthday <= CURRENT_DATE),
	category  	SMALLINT 		NOT NULL CHECK (category >= 1 AND category <= 10),
	type_id   	INTEGER 		NOT NULL REFERENCES Tourist_type(id),
	contact_id	INTEGER			NOT NULL REFERENCES Contacts(id)
);

CREATE TABLE Trainer (
	id                	INTEGER 	PRIMARY KEY  REFERENCES Tourist(id),
	salary            	MONEY		NOT NULL,
	hire_date         	DATE		NOT NULL,
	specialization_id 	INTEGER   	NOT NULL REFERENCES Specialization(id),
	section_id        	INTEGER 	NOT NULL REFERENCES Section(id)
);

CREATE TABLE SuperVisor (
	id          SERIAL 			PRIMARY KEY,
	full_name   VARCHAR(255) 	NOT NULL,
	salary      MONEY			NOT NULL,
	hire_date   DATE			NOT NULL,
	birthday    DATE			CHECK (birthday <= CURRENT_DATE),
	contact_id	INTEGER			NOT NULL REFERENCES Contacts(id)
);

CREATE TABLE Section (
	id            SERIAL 		PRIMARY KEY,
	name          VARCHAR(100) 	NOT NULL UNIQUE,
	description	  TEXT,
	supervisor_id INTEGER 		NOT NULL UNIQUE REFERENCES SuperVisor(id)
);

CREATE TABLE Group (
	id          SERIAL 			PRIMARY KEY,
	name        VARCHAR(100) 	NOT NULL UNIQUE,
	description TEXT,
	section_id  INTEGER 		NOT NULL, REFERENCES Section(id),
	trainer_id  INTEGER 		REFERENCES Trainer(id)
);

CREATE TABLE Tourist_Groups (
	tourist_id  INTEGER 	REFERENCES Tourist(id),
	group_id    INTEGER 	REFERENCES Group(id),
	PRIMARY KEY (tourist_id, group_id)
);

CREATE TABLE Training (
	id          SERIAL 			PRIMARY KEY,
	plane_date  TIMESTAMP		NOT NULL,
	real_date   TIMESTAMP,
	place       TEXT,
	duration    INTERVAL,
	trainer_id  INTEGER 		REFERENCES Trainer(id),
	group_id    INTEGER 		REFERENCES Group(id),
	section_id  INTEGER 		NOT NULL REFERENCES Section(id)
);

CREATE TABLE Attendance (
	tourist_id  INTEGER 	REFERENCES Tourist(id),
	training_id INTEGER 	REFERENCES Training(id),
	PRIMARY KEY (tourist_id, training_id)
);

CREATE TABLE Route (
	id                  SERIAL 			PRIMARY KEY,
	name                VARCHAR(100) 	NOT NULL UNIQUE,
	length_meters       INTEGER,
	duration      		INTERVAL,
	difficulty_category SMALLINT 		NOT NULL CHECK (category >= 1 AND category <= 10),
	description         TEXT
);

CREATE TABLE Hike (
	id              SERIAL 			PRIMARY KEY,
	plan_start_date TIMESTAMPTZ 	NOT NULL,
	real_start_date TIMESTAMPTZ,
	real_end_date   TIMESTAMPTZ,
	is_planned      BOOLEAN			DEFAULT: 0
	hike_type_id    INTEGER 		NOT NULL, REFERENCES Hike_type(id),
	instructor_id   INTEGER 		NOT NULL, REFERENCES Tourist(id),
	route_id        INTEGER 		NOT NULL, REFERENCES Route(id)
);

CREATE TABLE Diary (
	id      SERIAL 			PRIMARY KEY,
	date    TIMESTAMPTZ		NOT NULL,
	content TEXT			NOT NULL,
	hike_id INTEGER 		NOT NULL, REFERENCES Hike(id)
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
	point_number  SMALLINT	CHECK (point_number >= 1),
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
