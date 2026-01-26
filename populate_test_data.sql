-- ==========================================
-- Art School Management System - Test Data
-- ==========================================
-- Bu dosya veritabanını temizler ve örnek veriler ekler.
-- pgAdmin'de Query Tool ile çalıştırın.

-- 1. Mevcut verileri temizle
TRUNCATE TABLE attendance CASCADE;
TRUNCATE TABLE skill_tests CASCADE;
TRUNCATE TABLE payments CASCADE;
TRUNCATE TABLE enrollments CASCADE;
TRUNCATE TABLE sessions CASCADE;
TRUNCATE TABLE courses CASCADE;
TRUNCATE TABLE students CASCADE;
TRUNCATE TABLE instructors CASCADE;

-- ==========================================
-- 2. INSTRUCTORS (6 eğitmen)
-- ==========================================
INSERT INTO instructors (first_name, last_name, email, phone, specialization) VALUES
('Leonardo', 'Da Vinci', 'leonardo.davinci@artschool.com', '+1-555-0101', 'Renaissance Art'),
('Pablo', 'Picasso', 'pablo.picasso@artschool.com', '+1-555-0102', 'Modern Art'),
('Frida', 'Kahlo', 'frida.kahlo@artschool.com', '+1-555-0103', 'Surrealism'),
('Vincent', 'Van Gogh', 'vincent.vangogh@artschool.com', '+1-555-0104', 'Post-Impressionism'),
('Claude', 'Monet', 'claude.monet@artschool.com', '+1-555-0105', 'Impressionism'),
('Michelangelo', 'Buonarroti', 'michelangelo@artschool.com', '+1-555-0106', 'Sculpture');

-- ==========================================
-- 3. STUDENTS (15 öğrenci - 5 per level)
-- ==========================================
INSERT INTO students (first_name, last_name, email, phone, date_of_birth, skill_level) VALUES
('Alice', 'Johnson', 'alice.j@email.com', '+1-555-1001', '2005-03-15', 'BEGINNER'),
('Bob', 'Smith', 'bob.s@email.com', '+1-555-1002', '2004-07-22', 'BEGINNER'),
('Carol', 'Williams', 'carol.w@email.com', '+1-555-1003', '2006-01-10', 'BEGINNER'),
('David', 'Brown', 'david.b@email.com', '+1-555-1004', '2005-09-05', 'BEGINNER'),
('Emma', 'Davis', 'emma.d@email.com', '+1-555-1005', '2004-11-30', 'BEGINNER'),
('Frank', 'Miller', 'frank.m@email.com', '+1-555-1006', '2003-04-18', 'INTERMEDIATE'),
('Grace', 'Wilson', 'grace.w@email.com', '+1-555-1007', '2002-08-25', 'INTERMEDIATE'),
('Henry', 'Moore', 'henry.m@email.com', '+1-555-1008', '2003-12-12', 'INTERMEDIATE'),
('Isabel', 'Taylor', 'isabel.t@email.com', '+1-555-1009', '2002-05-20', 'INTERMEDIATE'),
('Jack', 'Anderson', 'jack.a@email.com', '+1-555-1010', '2003-10-08', 'INTERMEDIATE'),
('Kate', 'Thomas', 'kate.t@email.com', '+1-555-1011', '2001-02-14', 'ADVANCED'),
('Liam', 'Jackson', 'liam.j@email.com', '+1-555-1012', '2000-06-07', 'ADVANCED'),
('Maya', 'White', 'maya.w@email.com', '+1-555-1013', '2001-09-22', 'ADVANCED'),
('Noah', 'Harris', 'noah.h@email.com', '+1-555-1014', '2000-11-15', 'ADVANCED'),
('Olivia', 'Martin', 'olivia.m@email.com', '+1-555-1015', '2001-03-28', 'ADVANCED');

-- ==========================================
-- 4. COURSES (9 kurs - dinamik instructor_id)
-- ==========================================
INSERT INTO courses (name, description, term, skill_level, instructor_id, max_capacity, fee, start_date, end_date)
SELECT 'Drawing Fundamentals', 'Introduction to basic drawing techniques', 'SUMMER', 'BEGINNER', 
       (SELECT id FROM instructors WHERE last_name = 'Da Vinci'), 15, 299.99, '2024-02-01', '2024-05-31';

INSERT INTO courses (name, description, term, skill_level, instructor_id, max_capacity, fee, start_date, end_date)
SELECT 'Color Theory Basics', 'Understanding color and its applications', 'SUMMER', 'BEGINNER',
       (SELECT id FROM instructors WHERE last_name = 'Monet'), 12, 249.99, '2024-02-01', '2024-05-31';

INSERT INTO courses (name, description, term, skill_level, instructor_id, max_capacity, fee, start_date, end_date)
SELECT 'Art History 101', 'Survey of art movements and masters', 'WINTER', 'BEGINNER',
       (SELECT id FROM instructors WHERE last_name = 'Picasso'), 20, 199.99, '2024-09-01', '2024-12-15';

INSERT INTO courses (name, description, term, skill_level, instructor_id, max_capacity, fee, start_date, end_date)
SELECT 'Portrait Painting', 'Techniques for realistic portraits', 'SUMMER', 'INTERMEDIATE',
       (SELECT id FROM instructors WHERE last_name = 'Van Gogh'), 10, 499.99, '2024-02-01', '2024-05-31';

INSERT INTO courses (name, description, term, skill_level, instructor_id, max_capacity, fee, start_date, end_date)
SELECT 'Abstract Art Workshop', 'Exploring abstract expression', 'SUMMER', 'INTERMEDIATE',
       (SELECT id FROM instructors WHERE last_name = 'Picasso'), 12, 399.99, '2024-06-01', '2024-08-15';

INSERT INTO courses (name, description, term, skill_level, instructor_id, max_capacity, fee, start_date, end_date)
SELECT 'Digital Art Foundations', 'Introduction to digital painting', 'WINTER', 'INTERMEDIATE',
       (SELECT id FROM instructors WHERE last_name = 'Kahlo'), 15, 449.99, '2024-09-01', '2024-12-15';

INSERT INTO courses (name, description, term, skill_level, instructor_id, max_capacity, fee, start_date, end_date)
SELECT 'Master Oil Painting', 'Advanced oil painting techniques', 'SUMMER', 'ADVANCED',
       (SELECT id FROM instructors WHERE last_name = 'Da Vinci'), 8, 799.99, '2024-02-01', '2024-05-31';

INSERT INTO courses (name, description, term, skill_level, instructor_id, max_capacity, fee, start_date, end_date)
SELECT 'Sculpture Intensive', 'Advanced 3D sculpture workshop', 'SUMMER', 'ADVANCED',
       (SELECT id FROM instructors WHERE last_name = 'Buonarroti'), 6, 899.99, '2024-06-01', '2024-08-15';

INSERT INTO courses (name, description, term, skill_level, instructor_id, max_capacity, fee, start_date, end_date)
SELECT 'Contemporary Art Theory', 'Modern art philosophy and practice', 'WINTER', 'ADVANCED',
       (SELECT id FROM instructors WHERE last_name = 'Picasso'), 10, 599.99, '2024-09-01', '2024-12-15';

-- ==========================================
-- 5. SESSIONS (27 session - dinamik course_id)
-- ==========================================
-- Drawing Fundamentals sessions
INSERT INTO sessions (course_id, session_date, start_time, end_time, topic)
SELECT id, '2024-02-05', '10:00', '12:00', 'Introduction to Lines and Shapes' FROM courses WHERE name = 'Drawing Fundamentals';
INSERT INTO sessions (course_id, session_date, start_time, end_time, topic)
SELECT id, '2024-02-12', '10:00', '12:00', 'Perspective Drawing Basics' FROM courses WHERE name = 'Drawing Fundamentals';
INSERT INTO sessions (course_id, session_date, start_time, end_time, topic)
SELECT id, '2024-02-19', '10:00', '12:00', 'Shading and Light Techniques' FROM courses WHERE name = 'Drawing Fundamentals';

-- Color Theory sessions
INSERT INTO sessions (course_id, session_date, start_time, end_time, topic)
SELECT id, '2024-02-06', '14:00', '16:00', 'Primary and Secondary Colors' FROM courses WHERE name = 'Color Theory Basics';
INSERT INTO sessions (course_id, session_date, start_time, end_time, topic)
SELECT id, '2024-02-13', '14:00', '16:00', 'Color Harmony and Contrast' FROM courses WHERE name = 'Color Theory Basics';
INSERT INTO sessions (course_id, session_date, start_time, end_time, topic)
SELECT id, '2024-02-20', '14:00', '16:00', 'Warm vs Cool Colors' FROM courses WHERE name = 'Color Theory Basics';

-- Art History sessions
INSERT INTO sessions (course_id, session_date, start_time, end_time, topic)
SELECT id, '2024-09-05', '09:00', '11:00', 'Renaissance Masters' FROM courses WHERE name = 'Art History 101';
INSERT INTO sessions (course_id, session_date, start_time, end_time, topic)
SELECT id, '2024-09-12', '09:00', '11:00', 'Impressionism Movement' FROM courses WHERE name = 'Art History 101';
INSERT INTO sessions (course_id, session_date, start_time, end_time, topic)
SELECT id, '2024-09-19', '09:00', '11:00', 'Modern Art Evolution' FROM courses WHERE name = 'Art History 101';

-- Portrait Painting sessions
INSERT INTO sessions (course_id, session_date, start_time, end_time, topic)
SELECT id, '2024-02-07', '13:00', '16:00', 'Facial Proportions' FROM courses WHERE name = 'Portrait Painting';
INSERT INTO sessions (course_id, session_date, start_time, end_time, topic)
SELECT id, '2024-02-14', '13:00', '16:00', 'Skin Tones' FROM courses WHERE name = 'Portrait Painting';
INSERT INTO sessions (course_id, session_date, start_time, end_time, topic)
SELECT id, '2024-02-21', '13:00', '16:00', 'Expression' FROM courses WHERE name = 'Portrait Painting';

-- Abstract Art sessions
INSERT INTO sessions (course_id, session_date, start_time, end_time, topic)
SELECT id, '2024-06-03', '10:00', '13:00', 'Geometric Abstraction' FROM courses WHERE name = 'Abstract Art Workshop';
INSERT INTO sessions (course_id, session_date, start_time, end_time, topic)
SELECT id, '2024-06-10', '10:00', '13:00', 'Color Field Painting' FROM courses WHERE name = 'Abstract Art Workshop';
INSERT INTO sessions (course_id, session_date, start_time, end_time, topic)
SELECT id, '2024-06-17', '10:00', '13:00', 'Expressive Brushwork' FROM courses WHERE name = 'Abstract Art Workshop';

-- Digital Art sessions
INSERT INTO sessions (course_id, session_date, start_time, end_time, topic)
SELECT id, '2024-09-06', '15:00', '18:00', 'Digital Tools' FROM courses WHERE name = 'Digital Art Foundations';
INSERT INTO sessions (course_id, session_date, start_time, end_time, topic)
SELECT id, '2024-09-13', '15:00', '18:00', 'Layer Techniques' FROM courses WHERE name = 'Digital Art Foundations';
INSERT INTO sessions (course_id, session_date, start_time, end_time, topic)
SELECT id, '2024-09-20', '15:00', '18:00', 'Digital Color' FROM courses WHERE name = 'Digital Art Foundations';

-- Master Oil Painting sessions
INSERT INTO sessions (course_id, session_date, start_time, end_time, topic)
SELECT id, '2024-02-08', '09:00', '13:00', 'Underpainting' FROM courses WHERE name = 'Master Oil Painting';
INSERT INTO sessions (course_id, session_date, start_time, end_time, topic)
SELECT id, '2024-02-15', '09:00', '13:00', 'Glazing' FROM courses WHERE name = 'Master Oil Painting';
INSERT INTO sessions (course_id, session_date, start_time, end_time, topic)
SELECT id, '2024-02-22', '09:00', '13:00', 'Impasto' FROM courses WHERE name = 'Master Oil Painting';

-- Sculpture sessions
INSERT INTO sessions (course_id, session_date, start_time, end_time, topic)
SELECT id, '2024-06-04', '10:00', '15:00', 'Clay Modeling' FROM courses WHERE name = 'Sculpture Intensive';
INSERT INTO sessions (course_id, session_date, start_time, end_time, topic)
SELECT id, '2024-06-11', '10:00', '15:00', 'Armature' FROM courses WHERE name = 'Sculpture Intensive';
INSERT INTO sessions (course_id, session_date, start_time, end_time, topic)
SELECT id, '2024-06-18', '10:00', '15:00', 'Surface Finishing' FROM courses WHERE name = 'Sculpture Intensive';

-- Contemporary Art sessions
INSERT INTO sessions (course_id, session_date, start_time, end_time, topic)
SELECT id, '2024-09-07', '11:00', '14:00', 'Postmodernism' FROM courses WHERE name = 'Contemporary Art Theory';
INSERT INTO sessions (course_id, session_date, start_time, end_time, topic)
SELECT id, '2024-09-14', '11:00', '14:00', 'Conceptual Art' FROM courses WHERE name = 'Contemporary Art Theory';
INSERT INTO sessions (course_id, session_date, start_time, end_time, topic)
SELECT id, '2024-09-21', '11:00', '14:00', 'Installation Art' FROM courses WHERE name = 'Contemporary Art Theory';

-- ==========================================
-- 6. ENROLLMENTS (15 kayıt - dinamik id'ler)
-- ==========================================
INSERT INTO enrollments (student_id, course_id, status, enrollment_date)
SELECT s.id, c.id, 'ACTIVE', '2024-01-15'
FROM students s, courses c WHERE s.first_name = 'Alice' AND c.name = 'Drawing Fundamentals';

INSERT INTO enrollments (student_id, course_id, status, enrollment_date)
SELECT s.id, c.id, 'ACTIVE', '2024-01-16'
FROM students s, courses c WHERE s.first_name = 'Bob' AND c.name = 'Drawing Fundamentals';

INSERT INTO enrollments (student_id, course_id, status, enrollment_date)
SELECT s.id, c.id, 'ACTIVE', '2024-01-17'
FROM students s, courses c WHERE s.first_name = 'Carol' AND c.name = 'Color Theory Basics';

INSERT INTO enrollments (student_id, course_id, status, enrollment_date)
SELECT s.id, c.id, 'ACTIVE', '2024-01-18'
FROM students s, courses c WHERE s.first_name = 'David' AND c.name = 'Color Theory Basics';

INSERT INTO enrollments (student_id, course_id, status, enrollment_date)
SELECT s.id, c.id, 'ACTIVE', '2024-01-20'
FROM students s, courses c WHERE s.first_name = 'Frank' AND c.name = 'Portrait Painting';

INSERT INTO enrollments (student_id, course_id, status, enrollment_date)
SELECT s.id, c.id, 'ACTIVE', '2024-01-21'
FROM students s, courses c WHERE s.first_name = 'Grace' AND c.name = 'Portrait Painting';

INSERT INTO enrollments (student_id, course_id, status, enrollment_date)
SELECT s.id, c.id, 'ACTIVE', '2024-05-15'
FROM students s, courses c WHERE s.first_name = 'Henry' AND c.name = 'Abstract Art Workshop';

INSERT INTO enrollments (student_id, course_id, status, enrollment_date)
SELECT s.id, c.id, 'ACTIVE', '2024-01-25'
FROM students s, courses c WHERE s.first_name = 'Kate' AND c.name = 'Master Oil Painting';

INSERT INTO enrollments (student_id, course_id, status, enrollment_date)
SELECT s.id, c.id, 'ACTIVE', '2024-01-26'
FROM students s, courses c WHERE s.first_name = 'Liam' AND c.name = 'Master Oil Painting';

INSERT INTO enrollments (student_id, course_id, status, enrollment_date)
SELECT s.id, c.id, 'ACTIVE', '2024-05-20'
FROM students s, courses c WHERE s.first_name = 'Maya' AND c.name = 'Sculpture Intensive';

-- ==========================================
-- 7. PAYMENTS (dinamik enrollment_id)
-- ==========================================
INSERT INTO payments (enrollment_id, amount, status, payment_method, payment_date)
SELECT e.id, 299.99, 'COMPLETED', 'Credit Card', '2024-01-15'
FROM enrollments e 
JOIN students s ON e.student_id = s.id 
WHERE s.first_name = 'Alice';

INSERT INTO payments (enrollment_id, amount, status, payment_method, payment_date)
SELECT e.id, 299.99, 'COMPLETED', 'Cash', '2024-01-16'
FROM enrollments e 
JOIN students s ON e.student_id = s.id 
WHERE s.first_name = 'Bob';

INSERT INTO payments (enrollment_id, amount, status, payment_method, payment_date)
SELECT e.id, 249.99, 'PENDING', NULL, '2024-01-17'
FROM enrollments e 
JOIN students s ON e.student_id = s.id 
WHERE s.first_name = 'Carol';

INSERT INTO payments (enrollment_id, amount, status, payment_method, payment_date)
SELECT e.id, 249.99, 'COMPLETED', 'Debit Card', '2024-01-18'
FROM enrollments e 
JOIN students s ON e.student_id = s.id 
WHERE s.first_name = 'David';

INSERT INTO payments (enrollment_id, amount, status, payment_method, payment_date)
SELECT e.id, 499.99, 'COMPLETED', 'Credit Card', '2024-01-20'
FROM enrollments e 
JOIN students s ON e.student_id = s.id 
WHERE s.first_name = 'Frank';

INSERT INTO payments (enrollment_id, amount, status, payment_method, payment_date)
SELECT e.id, 499.99, 'PENDING', NULL, '2024-01-21'
FROM enrollments e 
JOIN students s ON e.student_id = s.id 
WHERE s.first_name = 'Grace';

INSERT INTO payments (enrollment_id, amount, status, payment_method, payment_date)
SELECT e.id, 399.99, 'COMPLETED', 'Bank Transfer', '2024-05-15'
FROM enrollments e 
JOIN students s ON e.student_id = s.id 
WHERE s.first_name = 'Henry';

INSERT INTO payments (enrollment_id, amount, status, payment_method, payment_date)
SELECT e.id, 799.99, 'COMPLETED', 'Credit Card', '2024-01-25'
FROM enrollments e 
JOIN students s ON e.student_id = s.id 
WHERE s.first_name = 'Kate';

INSERT INTO payments (enrollment_id, amount, status, payment_method, payment_date)
SELECT e.id, 799.99, 'PENDING', NULL, '2024-01-26'
FROM enrollments e 
JOIN students s ON e.student_id = s.id 
WHERE s.first_name = 'Liam';

INSERT INTO payments (enrollment_id, amount, status, payment_method, payment_date)
SELECT e.id, 899.99, 'COMPLETED', 'Credit Card', '2024-05-20'
FROM enrollments e 
JOIN students s ON e.student_id = s.id 
WHERE s.first_name = 'Maya';

-- ==========================================
-- 8. ATTENDANCE (dinamik enrollment_id ve session_id)
-- ==========================================
INSERT INTO attendance (enrollment_id, session_id, status, notes)
SELECT e.id, ss.id, 'PRESENT', 'Great participation'
FROM enrollments e 
JOIN students s ON e.student_id = s.id
JOIN courses c ON e.course_id = c.id
JOIN sessions ss ON ss.course_id = c.id
WHERE s.first_name = 'Alice' AND ss.topic = 'Introduction to Lines and Shapes';

INSERT INTO attendance (enrollment_id, session_id, status, notes)
SELECT e.id, ss.id, 'PRESENT', 'Showed improvement'
FROM enrollments e 
JOIN students s ON e.student_id = s.id
JOIN courses c ON e.course_id = c.id
JOIN sessions ss ON ss.course_id = c.id
WHERE s.first_name = 'Alice' AND ss.topic = 'Perspective Drawing Basics';

INSERT INTO attendance (enrollment_id, session_id, status, notes)
SELECT e.id, ss.id, 'LATE', 'Arrived 15 minutes late'
FROM enrollments e 
JOIN students s ON e.student_id = s.id
JOIN courses c ON e.course_id = c.id
JOIN sessions ss ON ss.course_id = c.id
WHERE s.first_name = 'Alice' AND ss.topic = 'Shading and Light Techniques';

INSERT INTO attendance (enrollment_id, session_id, status, notes)
SELECT e.id, ss.id, 'PRESENT', 'Excellent work'
FROM enrollments e 
JOIN students s ON e.student_id = s.id
JOIN courses c ON e.course_id = c.id
JOIN sessions ss ON ss.course_id = c.id
WHERE s.first_name = 'Bob' AND ss.topic = 'Introduction to Lines and Shapes';

INSERT INTO attendance (enrollment_id, session_id, status, notes)
SELECT e.id, ss.id, 'ABSENT', 'Called in sick'
FROM enrollments e 
JOIN students s ON e.student_id = s.id
JOIN courses c ON e.course_id = c.id
JOIN sessions ss ON ss.course_id = c.id
WHERE s.first_name = 'Bob' AND ss.topic = 'Perspective Drawing Basics';

INSERT INTO attendance (enrollment_id, session_id, status, notes)
SELECT e.id, ss.id, 'PRESENT', 'Professional level'
FROM enrollments e 
JOIN students s ON e.student_id = s.id
JOIN courses c ON e.course_id = c.id
JOIN sessions ss ON ss.course_id = c.id
WHERE s.first_name = 'Kate' AND ss.topic = 'Underpainting';

INSERT INTO attendance (enrollment_id, session_id, status, notes)
SELECT e.id, ss.id, 'PRESENT', 'Excellent technique'
FROM enrollments e 
JOIN students s ON e.student_id = s.id
JOIN courses c ON e.course_id = c.id
JOIN sessions ss ON ss.course_id = c.id
WHERE s.first_name = 'Kate' AND ss.topic = 'Glazing';

-- ==========================================
-- 9. SKILL TESTS (dinamik student_id)
-- ==========================================
INSERT INTO skill_tests (student_id, score, assigned_level, test_date, notes)
SELECT id, 65, 'BEGINNER', '2024-01-10', 'Shows promise' FROM students WHERE first_name = 'Alice';

INSERT INTO skill_tests (student_id, score, assigned_level, test_date, notes)
SELECT id, 72, 'BEGINNER', '2024-01-11', 'Good understanding' FROM students WHERE first_name = 'Bob';

INSERT INTO skill_tests (student_id, score, assigned_level, test_date, notes)
SELECT id, 82, 'INTERMEDIATE', '2024-01-15', 'Strong skills' FROM students WHERE first_name = 'Frank';

INSERT INTO skill_tests (student_id, score, assigned_level, test_date, notes)
SELECT id, 92, 'ADVANCED', '2024-01-20', 'Professional quality' FROM students WHERE first_name = 'Kate';

INSERT INTO skill_tests (student_id, score, assigned_level, test_date, notes)
SELECT id, 88, 'ADVANCED', '2024-01-21', 'Gallery-ready work' FROM students WHERE first_name = 'Liam';

-- ==========================================
-- SUMMARY / ÖZET
-- ==========================================
-- Instructors: 6
-- Students: 15 (5 BEGINNER, 5 INTERMEDIATE, 5 ADVANCED)
-- Courses: 9 (3 per level)
-- Sessions: 27 (3 per course)
-- Enrollments: 10 
-- Payments: 10 (6 COMPLETED, 4 PENDING)
-- Attendance: 7 records
-- Skill Tests: 5
