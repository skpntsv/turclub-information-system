SELECT 
    s.id, 
    s.full_name, 
    s.salary, 
    s.birthday, 
    s.hire_date
FROM 
    SuperVisor s
WHERE 
    (:salary IS NULL OR s.salary = :salary)
    AND (:birth_year IS NULL OR EXTRACT(YEAR FROM s.birthday) = :birth_year)
    AND (:age IS NULL OR EXTRACT(YEAR FROM AGE(s.birthday)) = :age)
    AND (:hire_year IS NULL OR EXTRACT(YEAR FROM s.hire_date) = :hire_year);
