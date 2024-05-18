SELECT
    DISTINCT tr.id AS ID,
    t.full_name AS ФИО
FROM
    Training trn
JOIN
    Trainer tr ON trn.trainer_id = tr.id
JOIN
    Tourist t ON tr.id = t.id
WHERE
    (trn.group_id = :group_id OR :group_id IS NULL) AND
    (trn.plane_date BETWEEN COALESCE(:start_date, trn.plane_date) AND COALESCE(:end_date, trn.plane_date))
ORDER BY tr.id;