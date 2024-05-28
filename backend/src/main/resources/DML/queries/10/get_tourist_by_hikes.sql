SELECT
    t.id AS ID,
    t.full_name AS ФИО
FROM
    Tourist t
JOIN
    Tourist_Groups tg ON tg.tourist_id = t.id
JOIN
    Groups g ON g.id = tg.group_id
JOIN
    Section s ON s.id = g.section_id
WHERE
    (:section_id IS NULL OR s.id = :section_id)
  AND (:group_id IS NULL OR g.id = :group_id)
  AND (COALESCE(:skill, 0) <= t.category)
ORDER BY t.id;
