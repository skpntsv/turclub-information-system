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
JOIN
    Hike_Tourists ht ON ht.tourist_id = t.id
JOIN
    Hike h ON h.id = ht.hike_id
WHERE
    (:section_id IS NULL OR s.id = :section_id)
  AND (:group_id IS NULL OR g.id = :group_id)
GROUP BY
    t.id, t.full_name
HAVING COUNT(DISTINCT h.route_id) = (SELECT COUNT(id) FROM Route)
ORDER BY t.id;