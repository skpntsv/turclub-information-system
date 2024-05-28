SELECT
    tr.id AS ID,
    t.full_name AS ФИО,
    SUM(EXTRACT(EPOCH FROM trn.duration)/3600) AS "Кол-во часов",
    COUNT(trn.id) AS "Кол-во тренировок"
FROM
    Training trn
JOIN
    Trainer tr ON trn.trainer_id = tr.id
JOIN
    Tourist t ON tr.id = t.id
WHERE
    (:trainer_id IS NULL OR tr.id = :trainer_id)
    AND (:section_id IS NULL OR tr.section_id = :section_id)
    AND (trn.plane_date >= COALESCE(:start_date, trn.plane_date) AND trn.plane_date <= COALESCE(:end_date, trn.plane_date))
GROUP BY
    tr.id, t.full_name;