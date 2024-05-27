SELECT
    t.id AS ID,
    t.full_name AS ФИО,
    h.id AS "ID похода",
    h.name AS Поход
FROM
    Tourist t
JOIN
    Hike_Tourists ht ON t.id = ht.tourist_id
LEFT JOIN
    Hike h ON ht.hike_id = h.id
WHERE
    (:section_id IS NULL OR t.id IN (
        SELECT tg.tourist_id
        FROM Tourist_Groups tg
        JOIN Groups g ON tg.group_id = g.id
        WHERE g.section_id = :section_id))
    AND (:group_id IS NULL OR t.id IN (
        SELECT tg.tourist_id
        FROM Tourist_Groups tg
        WHERE tg.group_id = :group_id))
    AND (:hike_id IS NULL OR ht.hike_id = :hike_id)
    AND (:route_id IS NULL OR h.route_id = :route_id)
    AND (:point_id IS NULL OR EXISTS (
        SELECT 1
        FROM Route_Checkpoints rc
        JOIN sсhedule_hike_checkpoints shc ON rc.id = shc.route_checkpoints_id
        WHERE shc.hike_id = h.id AND rc.checkpoint_id = :point_id))
    AND (:max_category IS NULL OR t.category <= :max_category)
    AND (:min_hikes IS NULL OR t.id IN (
        SELECT ht2.tourist_id
        FROM Hike_Tourists ht2
        GROUP BY ht2.tourist_id
        HAVING COUNT(ht2.hike_id) >= :min_hikes))
GROUP BY
    t.id, t.full_name, h.id, h.name
ORDER BY t.id;
