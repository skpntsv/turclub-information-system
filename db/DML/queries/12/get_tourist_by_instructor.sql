SELECT
    DISTINCT t.id,
    t.full_name
FROM
    Tourist t
JOIN
    Tourist_Groups tg ON tg.tourist_id = t.id
JOIN
    Groups g ON g.id = tg.group_id
JOIN
    Section s ON s.id = g.section_id
JOIN
    Hike_Tourists ht ON ht.tourist_id = t.id
JOIN
    Hike h ON h.id = ht.hike_id
WHERE
    (:section_id IS NULL OR s.id = :section_id)
  AND (:group_id IS NULL OR g.id = :group_id)
  AND h.instructor_id = g.trainer_id
ORDER BY t.id;
