CREATE TABLE IF NOT EXISTS Tourist_type (
	id    SERIAL 		PRIMARY KEY,
	name  VARCHAR(100) 	NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS Specialization (
	id    SERIAL 		PRIMARY KEY,
	name  VARCHAR(100) 	NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS Hike_type (
	id    SERIAL 		PRIMARY KEY,
	name  VARCHAR(100) 	NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS Contacts (
	id 				SERIAL 			PRIMARY KEY,
	email 			VARCHAR(256) 	NOT NULL CHECK (email ~* '^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$'),
	main_phone 		VARCHAR(15) 	NOT NULL CHECK (main_phone ~* '^(\+7|8)-\d{3}-\d{3}-\d{2}-\d{2}$'),
	reserve_phone	VARCHAR(15) 	CHECK (reserve_phone ~* '^(\+7|8)-\d{3}-\d{3}-\d{2}-\d{2}$'),
	emergency_phone VARCHAR(15) 	CHECK (emergency_phone ~* '^(\+7|8)-\d{3}-\d{3}-\d{2}-\d{2}$')
);

CREATE TABLE IF NOT EXISTS Tourist (
	id        	SERIAL 			PRIMARY KEY,
	full_name 	VARCHAR(255) 	NOT NULL,
	gender    	VARCHAR(7) 		NOT NULL CHECK (gender IN ('male', 'female')),
	birthday  	DATE     		NOT NULL CHECK (birthday <= CURRENT_DATE),
	category  	SMALLINT 		NOT NULL CHECK (category >= 1 AND category <= 10),
	type_id   	INTEGER 		NOT NULL REFERENCES Tourist_type(id),
	contact_id	INTEGER			NOT NULL REFERENCES Contacts(id)
);

CREATE TABLE IF NOT EXISTS Trainer (
	id                	INTEGER 	PRIMARY KEY  REFERENCES Tourist(id),
	salary            	MONEY		NOT NULL,
	hire_date         	DATE		NOT NULL,
	specialization_id 	INTEGER   	NOT NULL REFERENCES Specialization(id),
	section_id        	INTEGER 	NOT NULL REFERENCES Section(id)
);

CREATE TABLE IF NOT EXISTS SuperVisor (
	id          SERIAL 			PRIMARY KEY,
	full_name   VARCHAR(255) 	NOT NULL,
	salary      MONEY			NOT NULL,
	hire_date   DATE			NOT NULL,
	birthday    DATE			CHECK (birthday <= CURRENT_DATE),
	contact_id	INTEGER			NOT NULL REFERENCES Contacts(id)
);

CREATE TABLE IF NOT EXISTS Section (
	id            SERIAL 		PRIMARY KEY,
	name          VARCHAR(100) 	NOT NULL UNIQUE,
	description	  TEXT,
	supervisor_id INTEGER 		NOT NULL UNIQUE REFERENCES SuperVisor(id)
);

CREATE TABLE IF NOT EXISTS Group (
	id          SERIAL 			PRIMARY KEY,
	name        VARCHAR(100) 	NOT NULL UNIQUE,
	description TEXT,
	section_id  INTEGER 		NOT NULL, REFERENCES Section(id),
	trainer_id  INTEGER 		REFERENCES Trainer(id)
);

CREATE TABLE IF NOT EXISTS Tourist_Groups (
	tourist_id  INTEGER 	REFERENCES Tourist(id),
	group_id    INTEGER 	REFERENCES Group(id),
	PRIMARY KEY (tourist_id, group_id)
);

CREATE TABLE IF NOT EXISTS Training (
	id          SERIAL 			PRIMARY KEY,
	plane_date  TIMESTAMP		NOT NULL,
	real_date   TIMESTAMP,
	place       TEXT,
	duration    INTERVAL,
	trainer_id  INTEGER 		REFERENCES Trainer(id),
	group_id    INTEGER 		REFERENCES Group(id),
	section_id  INTEGER 		NOT NULL REFERENCES Section(id)
);

CREATE TABLE IF NOT EXISTS Attendance (
	tourist_id  INTEGER 	REFERENCES Tourist(id),
	training_id INTEGER 	REFERENCES Training(id),
	PRIMARY KEY (tourist_id, training_id)
);

CREATE TABLE IF NOT EXISTS Route (
	id                  SERIAL 			PRIMARY KEY,
	name                VARCHAR(100) 	NOT NULL UNIQUE,
	length_meters       INTEGER,
	duration      		INTERVAL,
	difficulty_category SMALLINT 		NOT NULL CHECK (category >= 1 AND category <= 10),
	description         TEXT
);

CREATE TABLE IF NOT EXISTS Hike (
	id              SERIAL 			PRIMARY KEY,
	plan_start_date TIMESTAMPTZ 	NOT NULL,
	real_start_date TIMESTAMPTZ,
	real_end_date   TIMESTAMPTZ,
	is_planned      BOOLEAN			NOT NULL DEFAULT false,
	hike_type_id    INTEGER 		NOT NULL, REFERENCES Hike_type(id),
	instructor_id   INTEGER 		NOT NULL, REFERENCES Tourist(id),
	route_id        INTEGER 		NOT NULL, REFERENCES Route(id)
    CONSTRAINT check_end_date_after_start_date 
        CHECK ((real_end_date IS NULL) OR (real_end_date > real_start_date))
);

CREATE TABLE IF NOT EXISTS Diary (
	id      SERIAL 			PRIMARY KEY,
	date    TIMESTAMPTZ		NOT NULL,
	content TEXT			NOT NULL,
	hike_id INTEGER 		NOT NULL, REFERENCES Hike(id)
);

CREATE TABLE IF NOT EXISTS Checkpoint (
	id          SERIAL 			PRIMARY KEY,
	description TEXT,
	type        VARCHAR(7) 		CHECK (type IN ('passing', 'rest')),
	latitude    NUMERIC(3, 7) 	CHECK (latitude >= -90 AND latitude <= 90),
	longitude   NUMERIC(4, 7) 	CHECK (longitude >= -180 AND longitude <= 180)
);

CREATE TABLE IF NOT EXISTS Route_Checkpoints (
	id            SERIAL 	UNIQUE,
	point_number  SMALLINT	CHECK (point_number >= 1),
	route_id      INTEGER 	REFERENCES Route(id),
	checkpoint_id INTEGER 	REFERENCES Checkpoint(id),
	PRIMARY KEY (point_number, route_id, checkpoint_id)
);

CREATE TABLE IF NOT EXISTS Sсhedule_hike_checkpoints (
	plan_date             TIMESTAMPTZ 		NOT NULL,
	real_date             TIMESTAMPTZ,
	hike_id               INTEGER 			REFERENCES Hike(id),
	route_checkpoints_id  INTEGER 			REFERENCES Route_Checkpoints(id),
	PRIMARY KEY (hike_id, route_checkpoints_id)
);

CREATE TABLE IF NOT EXISTS Hike_Tourists (
	tourist_id  INTEGER 	REFERENCES Tourist(id),
	hike_id     INTEGER 	REFERENCES Hike(id),
	PRIMARY KEY (tourist_id, hike_id)
);

CREATE TABLE IF NOT EXISTS Competition (
	id          SERIAL 			PRIMARY KEY,
	date        TIMESTAMPTZ		NOT NULL,
	name        VARCHAR(255) 	NOT NULL,
	place       VARCHAR(255)	NOT NULL,
	description TEXT
);

CREATE TABLE IF NOT EXISTS Tourist_Competitions (
	tourist_id      INTEGER 	REFERENCES Tourist(id),
	competition_id  INTEGER 	REFERENCES Competition(id),
	PRIMARY KEY (tourist_id, competition_id)
);


CREATE OR REPLACE FUNCTION update_tourist_level()
RETURNS TRIGGER AS
$$
DECLARE
    hike_difficulty SMALLINT;
BEGIN
    -- Получаем уровень сложности похода
    SELECT difficulty_category INTO hike_difficulty
    FROM Hike
    WHERE id = NEW.hike_id;

    -- Проверяем, есть ли дата окончания похода и is_planned равно 1
    IF NEW.real_end_date IS NOT NULL AND NEW.is_planned THEN
        -- Проверяем уровень каждого туриста
        FOR tourist_rec IN
            SELECT t.id, t.category
            FROM Tourist t
            JOIN Hike_Tourists ht ON t.id = ht.tourist_id
            WHERE ht.hike_id = NEW.hike_id
        LOOP
            IF tourist_rec.category < hike_difficulty THEN
                -- Повышаем уровень туриста до уровня похода
                UPDATE Tourist
                SET category = hike_difficulty
                WHERE id = tourist_rec.id;
            END IF;
        END LOOP;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_tourist_level_trigger
AFTER INSERT OR UPDATE ON Hike
FOR EACH ROW
EXECUTE FUNCTION update_tourist_level();
