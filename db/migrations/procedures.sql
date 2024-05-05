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

