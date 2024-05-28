SELECT
    DISTINCT tr.id AS ID,
    t.full_name AS ФИО,
    CASE
        WHEN t.gender = 'female' THEN 'Женский'
        WHEN t.gender = 'male' THEN 'Мужской'
        ELSE t.gender
    END AS Пол,
    t.birthday AS "Дата рождения",
    trn.real_date AS "Дата тренировки"
FROM
    Training trn
JOIN Trainer tr ON trn.trainer_id = tr.id
JOIN Tourist t ON tr.id = t.id
WHERE
    trn.real_date IS NOT NULL AND
    (trn.group_id = :group_id OR :group_id IS NULL) AND
    (trn.real_date >= COALESCE(:start_date, trn.real_date) AND trn.real_date <= COALESCE(:end_date, trn.real_date))
ORDER BY tr.id;