CREATE OR REPLACE FUNCTION check_hike_category_change()
RETURNS TRIGGER AS $$
DECLARE
    d_category SMALLINT;
BEGIN
    -- Проверяем, был ли изменен тип похода с непланового на плановый и закончился ли поход
    IF (OLD.is_planned = false AND NEW.is_planned = true AND NEW.real_end_date IS NOT NULL) OR
       (OLD.real_end_date IS NULL AND NEW.real_end_date IS NOT NULL AND NEW.is_planned = true) THEN
        -- Смотрим какая была сложность у похода
        SELECT difficulty_category into d_category
                                   from Route
                                   WHERE id = NEW.route_id;
        -- Повышаем уровень сложности у всех участников этого похода
        UPDATE Tourist
        SET category = GREATEST(category, d_category)
        FROM Hike_Tourists
        WHERE Hike_Tourists.hike_id = NEW.id
        AND Hike_Tourists.tourist_id = Tourist.id;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER hike_category_change_trigger
AFTER INSERT OR UPDATE OF is_planned, real_end_date ON Hike
FOR EACH ROW
EXECUTE FUNCTION check_hike_category_change();

------------------------------------------------------------

CREATE OR REPLACE FUNCTION check_instructor_assignment()
RETURNS TRIGGER AS $$
DECLARE
    d_category SMALLINT;
    trainer_id INTEGER;
BEGIN
    -- Находим, какой айди принадлежит типу "тренер"
    SELECT id INTO trainer_id FROM Tourist_type WHERE LOWER(name) = 'тренер';
    -- Проверяем, что инструктор - это спортсмен или тренер
    IF NOT EXISTS (SELECT 1 FROM Tourist WHERE id = NEW.instructor_id AND tourist.type_id = trainer_id) THEN
        RAISE EXCEPTION 'Инструктор должен быть спортсменом или тренером';
    END IF;
    -- Смотрим какая была сложность у похода
    SELECT difficulty_category into d_category
                               from Route
                               WHERE id = NEW.route_id;
    IF NOT EXISTS (SELECT 1 FROM Tourist WHERE id = NEW.instructor_id AND tourist.category >= d_category) THEN
        RAISE EXCEPTION 'У инструктора недостаточная категория сложности для данного похода';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER instructor_assignment_trigger
BEFORE INSERT OR UPDATE OF instructor_id ON Hike
FOR EACH ROW
EXECUTE FUNCTION check_instructor_assignment();

---------------------------------------------------

CREATE OR REPLACE FUNCTION check_participant_in_hike_limit()
RETURNS TRIGGER AS $$
DECLARE
    participant_count INTEGER;
BEGIN
    -- Получаем количество участников для данного похода
    SELECT COUNT(*)
    INTO participant_count
    FROM Hike_Tourists
    WHERE hike_id = NEW.hike_id;

    -- Проверяем, что количество участников не превышает 15
    IF participant_count >= 15 THEN
        RAISE EXCEPTION 'Максимальное количество участников в походе - 15';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER check_participant_in_hike_limit_trigger
BEFORE INSERT OR UPDATE OF hike_id ON Hike_Tourists
FOR EACH ROW
EXECUTE FUNCTION check_participant_in_hike_limit();

----------------------------------------------------------------

CREATE OR REPLACE FUNCTION set_termination_date()
RETURNS TRIGGER AS $$
DECLARE
    old_type_name TEXT;
    new_type_name TEXT;
BEGIN
    -- Получаем старый и новый тип турита
    SELECT name INTO old_type_name FROM tourist_type WHERE id = OLD.type_id;
    SELECT name INTO new_type_name FROM tourist_type WHERE id = NEW.type_id;

    -- Проверяем, был ли старый тип "тренер" и новый тип не "тренер"
    IF UPPER(old_type_name) = 'ТРЕНЕР' AND UPPER(new_type_name) != 'ТРЕНЕР' THEN
        -- Устанавливаем дату увольнения текущей датой
        UPDATE trainer
        SET termination_date = CURRENT_DATE
        WHERE id = OLD.id;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_set_termination_date
AFTER UPDATE OF type_id ON Tourist
FOR EACH ROW
EXECUTE FUNCTION set_termination_date();