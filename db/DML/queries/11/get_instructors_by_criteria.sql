SELECT
    DISTINCT t.id AS ID,
    t.full_name AS ФИО,
    tp.name AS "Тип туриста"
FROM
    Tourist t
JOIN
    tourist_type tp On t.type_id = tp.id
JOIN
    Hike h ON h.instructor_id = t.id
LEFT JOIN
    Sсhedule_hike_checkpoints shc ON shc.hike_id = h.id
LEFT JOIN
    Route_Checkpoints rc ON rc.id = shc.route_checkpoints_id
WHERE
    (:category IS NULL OR t.category = :category)
  AND (:tourist_type IS NULL OR tp.id = :tourist_type)
  AND (:hike_count IS NULL OR (SELECT COUNT(DISTINCT h2.id)
                               FROM Hike h2
                               WHERE h2.instructor_id = t.id) >= :hike_count)
  AND (:hike_id IS NULL OR h.id = :hike_id)
  AND (:route_id IS NULL OR h.route_id = :route_id)
  AND (:checkpoint_id IS NULL OR rc.checkpoint_id = :checkpoint_id)
ORDER BY t.id;
