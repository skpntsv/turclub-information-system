SELECT
    tr.id AS ID,
    t.full_name AS "ФИО тренера",
    CASE
        WHEN t.gender = 'female' THEN 'Женский'
        WHEN t.gender = 'male' THEN 'Мужской'
        ELSE t.gender
    END AS Пол,
    t.birthday AS "Дата рождения",
    tr.salary AS "Зарплата",
    sp.name AS "Специализация",
    s.name AS "Секция"
FROM
    Trainer tr
    JOIN Tourist t ON tr.id = t.id
    JOIN Section s ON tr.section_id = s.id
    JOIN Specialization sp ON tr.specialization_id = sp.id
WHERE
    tr.termination_date IS NULL AND -- Условие для вывода только действующих тренеров
    (:section_id IS NULL OR s.id = :section_id) AND
    (:gender IS NULL OR t.gender = :gender) AND
    (:age IS NULL OR DATE_PART('year', AGE(t.birthday)) = :age) AND
    (:min_salary IS NULL OR tr.salary >= :min_salary) AND
    (:max_salary IS NULL OR tr.salary <= :max_salary) AND
    (:specialization_id IS NULL OR sp.id = :specialization_id)
ORDER BY
    t.full_name;