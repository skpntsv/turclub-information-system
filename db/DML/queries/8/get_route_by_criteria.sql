SELECT
    r.id AS ID,
    r.name AS Название
FROM
    Route r
        LEFT JOIN
    Hike h ON h.route_id = r.id
        LEFT JOIN
    Hike_Tourists ht ON ht.hike_id = h.id
        LEFT JOIN
    Tourist t ON t.id = ht.tourist_id
        LEFT JOIN
    Tourist_Groups tg ON tg.tourist_id = t.id
        LEFT JOIN
    Groups g ON g.id = tg.group_id
WHERE
    h.real_start_date IS NOT NULL AND h.real_end_date IS NOT NULL
  AND (:section_id IS NULL OR g.section_id = :section_id)
  AND (:instructor_id IS NULL OR h.instructor_id = :instructor_id)
  AND (h.real_start_date >= COALESCE(:start_date, h.real_start_date) AND h.real_start_date <= COALESCE(:end_date, h.real_start_date))
GROUP BY r.id, r.name
HAVING COUNT(DISTINCT h.id) >= COALESCE(:group_count, 0)
ORDER BY r.id;