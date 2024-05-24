SELECT
    COUNT(t.id) AS "Количество туристов"
FROM
    Tourist t
    JOIN Tourist_Groups tg ON t.id = tg.tourist_id
    JOIN Groups g ON tg.group_id = g.id
    JOIN Section s ON g.section_id = s.id
WHERE
    (s.id = :section_id OR :section_id IS NULL) AND
    (g.id = :group_id OR :group_id IS NULL) AND
    (t.gender = :gender OR :gender IS NULL) AND
    (EXTRACT(YEAR FROM t.birthday) BETWEEN COALESCE(:birthday_start_year, EXTRACT(YEAR FROM t.birthday)) AND COALESCE(:birthday_end_year, EXTRACT(YEAR FROM t.birthday))) AND
    (DATE_PART('year', age(t.birthday)) BETWEEN COALESCE(:min_age, DATE_PART('year', age(t.birthday))) AND COALESCE(:max_age, DATE_PART('year', age(t.birthday))));

SELECT 
    COUNT(t.id) AS "Количество туристов"
FROM 
    Tourist t
    JOIN Tourist_Groups tg ON t.id = tg.tourist_id
    JOIN Groups g ON tg.group_id = g.id
    JOIN Section s ON g.section_id = s.id
WHERE 
    (:section_id IS NULL OR s.id = :section_id) AND 
    (:group_id IS NULL OR g.id = :group_id) AND 
    (:gender IS NULL OR t.gender = :gender) AND 
    (:birthday_start_year IS NULL OR EXTRACT(YEAR FROM t.birthday) >= :birthday_start_year) AND 
    (:birthday_end_year IS NULL OR EXTRACT(YEAR FROM t.birthday) <= :birthday_end_year) AND 
    (:min_age IS NULL OR DATE_PART('year', age(t.birthday)) >= :min_age) AND 
    (:max_age IS NULL OR DATE_PART('year', age(t.birthday)) <= :max_age);