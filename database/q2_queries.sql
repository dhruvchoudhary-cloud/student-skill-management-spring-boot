CREATE DATABASE IF NOT EXISTS student_management;
USE student_management;

-- JPA creates the students and student_skills tables when the application starts.
-- The following queries implement the SQL part of Q2.

-- 1. Average score
SELECT student_id, student_name, (programming_score + mathematics_score) / 2 AS average_score
FROM students;

-- 2. Performance category
SELECT student_id, student_name,
       (programming_score + mathematics_score) / 2 AS average_score,
       CASE
         WHEN (programming_score + mathematics_score) / 2 >= 85 THEN 'Excellent'
         WHEN (programming_score + mathematics_score) / 2 >= 70 THEN 'Good'
         WHEN (programming_score + mathematics_score) / 2 >= 50 THEN 'Satisfactory'
         ELSE 'Needs Improvement'
       END AS performance_category
FROM students;

-- 3. Stronger subject
SELECT student_id, student_name,
       CASE
         WHEN programming_score > mathematics_score THEN 'Programming'
         WHEN mathematics_score > programming_score THEN 'Mathematics'
         ELSE 'Both equal'
       END AS stronger_subject
FROM students;

-- 4. Search
SELECT * FROM students WHERE student_id = 'STU001';
SELECT * FROM students WHERE email = 'aisha@example.com';
SELECT * FROM students WHERE course_name = 'B.Tech CSE';

-- 5. Merit list
SELECT student_id, student_name, (programming_score + mathematics_score) / 2 AS average_score
FROM students ORDER BY average_score DESC;

-- 6. Students with exactly the same skill set (compare normalized sorted skill strings)
SELECT grouped_skills, GROUP_CONCAT(student_id) AS student_ids
FROM (
    SELECT student_id, GROUP_CONCAT(LOWER(TRIM(skill_name)) ORDER BY LOWER(TRIM(skill_name)) SEPARATOR ',') AS grouped_skills
    FROM student_skills GROUP BY student_id
) x
GROUP BY grouped_skills
HAVING COUNT(*) > 1;

-- 7. Programming below 50 and mathematics at least 75
SELECT * FROM students WHERE programming_score < 50 AND mathematics_score >= 75;

-- 8. Duplicate IDs prevented by PRIMARY KEY; email also unique.
-- Score range is validated in Java (0..100); optional DB CHECK constraints can also be added.
