SELECT COUNT(t.id) AS "Количество туристов"
FROM Tourist t
         LEFT JOIN Tourist_Groups tg ON t.id = tg.tourist_id
         LEFT JOIN Groups g ON tg.group_id = g.id
         LEFT JOIN Section s ON g.section_id = s.id
WHERE (s.id = :section_id OR :section_id IS NULL)
  AND (g.id = :group_id OR :group_id IS NULL)
  AND (t.gender = :gender OR :gender IS NULL)
  AND (EXTRACT(YEAR FROM t.birthday) BETWEEN COALESCE(:birthday_start_year, EXTRACT(YEAR FROM t.birthday))
    AND COALESCE(:birthday_end_year, EXTRACT(YEAR FROM t.birthday)))
  AND (DATE_PART('year', age(t.birthday)) BETWEEN COALESCE(:min_age, DATE_PART('year', age(t.birthday)))
    AND COALESCE(:max_age, DATE_PART('year', age(t.birthday))));