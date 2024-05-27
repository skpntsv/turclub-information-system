SELECT
    COUNT(DISTINCT r.id) AS "Кол-во маршрутов"
FROM
    Route r
LEFT JOIN
    Route_Checkpoints rc ON rc.route_id = r.id
WHERE
    (:checkpoint_id IS NULL OR rc.checkpoint_id = :checkpoint_id)
  AND (:length IS NULL OR r.length_meters > :length)
  AND (:category IS NULL OR r.difficulty_category > :category);

