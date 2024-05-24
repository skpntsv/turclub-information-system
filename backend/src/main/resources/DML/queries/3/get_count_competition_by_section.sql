SELECT
    COUNT(c.id) AS "Количество соревнований"
FROM
    Competition c
        JOIN Tourist_Competitions tc ON c.id = tc.competition_id
        JOIN Tourist t ON tc.tourist_id = t.id
        JOIN Tourist_Groups tg ON t.id = tg.tourist_id
        JOIN Groups g ON tg.group_id = g.id
WHERE
    (:section_id IS NULL OR g.section_id = :section_id);