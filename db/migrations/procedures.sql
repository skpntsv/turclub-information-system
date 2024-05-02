

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