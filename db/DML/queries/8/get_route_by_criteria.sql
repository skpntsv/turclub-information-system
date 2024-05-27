SELECT
    r.id AS ID,
    r.name AS Название
FROM
    Route r
JOIN
    Hike h ON h.route_id = r.id
JOIN
    Hike_Tourists ht ON ht.hike_id = h.id
JOIN
    Tourist t ON t.id = ht.tourist_id
JOIN
    Tourist_Groups tg ON tg.tourist_id = t.id
JOIN
    Groups g ON g.id = tg.group_id
WHERE (:section_id IS NULL OR g.section_id = :section_id)
  AND (:start_date IS NULL OR h.real_start_date >= :start_date)
  AND (:end_date IS NULL OR h.real_start_date <= :end_date)
  AND (:instructor_id IS NULL OR h.instructor_id = :instructor_id)
GROUP BY r.id, r.name
HAVING COUNT(DISTINCT h.id) >= COALESCE(:group_count, 0)
ORDER BY r.id;
