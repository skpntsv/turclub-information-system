SELECT
    t.id as ID,
    t.full_name AS ФИО,
    tt.name AS Тип_туриста,
    CASE
        WHEN t.gender = 'female' THEN 'Женский'
        WHEN t.gender = 'male' THEN 'Мужской'
        ELSE t.gender
    END AS Пол,
    t.birthday AS "Дата рождения",
    t.category AS Категория,
    c.email AS Email,
    c.main_phone AS Основной_телефон,
    c.reserve_phone AS "Резервный телефон",
    c.emergency_phone AS "Срочный телефон",
    g.name AS "Название группы",
    s.name AS "Название секции"
FROM
    Tourist t
    JOIN Tourist_Groups tg ON t.id = tg.tourist_id
    JOIN Groups g ON tg.group_id = g.id
    JOIN Section s ON g.section_id = s.id
    JOIN Tourist_type tt ON t.type_id = tt.id
    JOIN Contacts c ON t.contact_id = c.id
WHERE
    (s.id = :section_id OR :section_id IS NULL) AND
    (g.id = :group_id OR :group_id IS NULL) AND
    (t.gender = :gender OR :gender IS NULL) AND
    (EXTRACT(YEAR FROM t.birthday) BETWEEN COALESCE(:birthday_start_year, EXTRACT(YEAR FROM t.birthday)) AND COALESCE(:birthday_end_year, EXTRACT(YEAR FROM t.birthday))) AND
    (DATE_PART('year', age(t.birthday)) BETWEEN COALESCE(:min_age, DATE_PART('year', age(t.birthday))) AND COALESCE(:max_age, DATE_PART('year', age(t.birthday))))
ORDER BY
    t.id;
