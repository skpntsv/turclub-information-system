SELECT
    COUNT(t.id) AS "Количество туристов"
FROM
    Tourist t
    LEFT JOIN Tourist_Groups tg ON t.id = tg.tourist_id
    LEFT JOIN Groups g ON tg.group_id = g.id
    LEFT JOIN Section s ON g.section_id = s.id
WHERE
    (:section_id IS NULL OR s.id = :section_id) AND
    (:group_id IS NULL OR g.id = :group_id) AND
    (:gender IS NULL OR t.gender = :gender) AND
    (:birthday_start_year IS NULL OR EXTRACT(YEAR FROM t.birthday) >= :birthday_start_year) AND
    (:birthday_end_year IS NULL OR EXTRACT(YEAR FROM t.birthday) <= :birthday_end_year) AND
    (:min_age IS NULL OR DATE_PART('year', age(t.birthday)) >= :min_age) AND
    (:max_age IS NULL OR DATE_PART('year', age(t.birthday)) <= :max_age);