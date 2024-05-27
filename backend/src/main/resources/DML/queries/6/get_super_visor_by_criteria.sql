SELECT 
    s.id AS ID, 
    s.full_name AS ФИО, 
    s.salary AS Зарплата, 
    s.hire_date AS "Дата приёма на работу", 
    c.email AS "Эл. почта"
FROM 
    SuperVisor s
JOIN 
    Contacts c ON s.contact_id = c.id
WHERE 
    (:min_salary IS NULL OR s.salary >= :min_salary)
    AND (:max_salary IS NULL OR s.salary <= :max_salary)
    AND (:min_birth_year IS NULL OR EXTRACT(YEAR FROM s.birthday) >= :min_birth_year)
    AND (:max_birth_year IS NULL OR EXTRACT(YEAR FROM s.birthday) <= :max_birth_year)
    AND (s.hire_date >= COALESCE(:min_hire_date, s.hire_date) AND s.hire_date <= COALESCE(:max_hire_date, s.hire_date))
