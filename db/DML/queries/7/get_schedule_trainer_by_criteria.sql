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
    AND ((:start_date IS NULL OR :end_date IS NULL) OR trn.plane_date BETWEEN :start_date AND :end_date)
GROUP BY
    tr.id, t.full_name;