SELECT
    t.id as ID,
    t.full_name AS ФИО,
    tt.name AS "Тип туриста",
    CASE
        WHEN t.gender = 'female' THEN 'Женский'
        WHEN t.gender = 'male' THEN 'Мужской'
        ELSE t.gender
    END AS Пол,
    t.birthday AS "Дата рождения",
    t.category AS Категория,
    c.email AS Почта,
    c.main_phone AS "Основной телефон",
    c.reserve_phone AS "Резервный телефон",
    c.emergency_phone AS "Экстренный телефон",
    g.name AS "Группа",
    s.name AS "Секция"
FROM
    Tourist t
    LEFT JOIN Tourist_Groups tg ON t.id = tg.tourist_id
    LEFT JOIN Groups g ON tg.group_id = g.id
    LEFT JOIN Section s ON g.section_id = s.id
    JOIN Tourist_type tt ON t.type_id = tt.id
    JOIN Contacts c ON t.contact_id = c.id
WHERE
    (s.id = :section_id OR :section_id IS NULL) AND
    (g.id = :group_id OR :group_id IS NULL) AND
    (t.gender = :gender OR :gender IS NULL) AND
    (:birthday_start_year IS NULL OR EXTRACT(YEAR FROM t.birthday) >= :birthday_start_year) AND
    (:birthday_end_year IS NULL OR EXTRACT(YEAR FROM t.birthday) <= :birthday_end_year) AND
    (:min_age IS NULL OR DATE_PART('year', age(t.birthday)) >= :min_age) AND
    (:max_age IS NULL OR DATE_PART('year', age(t.birthday)) <= :max_age)
ORDER BY
    t.full_name;
