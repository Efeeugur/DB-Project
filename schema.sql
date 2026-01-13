-- Art School Management System
-- PostgreSQL Database Schema
-- Database: DB-Project

-- Drop tables if exist (for clean installation)
DROP TABLE IF EXISTS attendance CASCADE;
DROP TABLE IF EXISTS payments CASCADE;
DROP TABLE IF EXISTS skill_tests CASCADE;
DROP TABLE IF EXISTS sessions CASCADE;
DROP TABLE IF EXISTS enrollments CASCADE;
DROP TABLE IF EXISTS courses CASCADE;
DROP TABLE IF EXISTS instructors CASCADE;
DROP TABLE IF EXISTS students CASCADE;

-- Create Students table
CREATE TABLE students (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(20),
    date_of_birth VARCHAR(20),
    skill_level VARCHAR(20) NOT NULL DEFAULT 'BEGINNER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_skill_level CHECK (skill_level IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED'))
);

-- Create Instructors table
CREATE TABLE instructors (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(20),
    specialization VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create Courses table
CREATE TABLE courses (
    id SERIAL PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    term VARCHAR(20) NOT NULL,
    skill_level VARCHAR(20) NOT NULL,
    instructor_id INTEGER NOT NULL,
    max_capacity INTEGER NOT NULL DEFAULT 20,
    fee DECIMAL(10, 2) NOT NULL DEFAULT 0.00,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_instructor FOREIGN KEY (instructor_id) REFERENCES instructors(id) ON DELETE CASCADE,
    CONSTRAINT chk_term CHECK (term IN ('SUMMER', 'WINTER')),
    CONSTRAINT chk_course_skill_level CHECK (skill_level IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED')),
    CONSTRAINT chk_dates CHECK (end_date >= start_date)
);

-- Create Enrollments table
CREATE TABLE enrollments (
    id SERIAL PRIMARY KEY,
    student_id INTEGER NOT NULL,
    course_id INTEGER NOT NULL,
    enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    CONSTRAINT fk_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    CONSTRAINT chk_status CHECK (status IN ('ACTIVE', 'COMPLETED', 'DROPPED')),
    CONSTRAINT uk_student_course UNIQUE (student_id, course_id)
);

-- Create Sessions table
CREATE TABLE sessions (
    id SERIAL PRIMARY KEY,
    course_id INTEGER NOT NULL,
    session_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    topic VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_session_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    CONSTRAINT chk_session_times CHECK (end_time > start_time)
);

-- Create Attendance table
CREATE TABLE attendance (
    id SERIAL PRIMARY KEY,
    enrollment_id INTEGER NOT NULL,
    session_id INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL,
    notes TEXT,
    recorded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_enrollment FOREIGN KEY (enrollment_id) REFERENCES enrollments(id) ON DELETE CASCADE,
    CONSTRAINT fk_session FOREIGN KEY (session_id) REFERENCES sessions(id) ON DELETE CASCADE,
    CONSTRAINT chk_attendance_status CHECK (status IN ('PRESENT', 'ABSENT', 'LATE', 'EXCUSED')),
    CONSTRAINT uk_enrollment_session UNIQUE (enrollment_id, session_id)
);

-- Create Payments table
CREATE TABLE payments (
    id SERIAL PRIMARY KEY,
    enrollment_id INTEGER NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    payment_method VARCHAR(50),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    CONSTRAINT fk_payment_enrollment FOREIGN KEY (enrollment_id) REFERENCES enrollments(id) ON DELETE CASCADE,
    CONSTRAINT chk_payment_status CHECK (status IN ('PENDING', 'COMPLETED', 'REFUNDED', 'FAILED'))
);

-- Create Skill Tests table
CREATE TABLE skill_tests (
    id SERIAL PRIMARY KEY,
    student_id INTEGER NOT NULL,
    test_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    score INTEGER NOT NULL,
    assigned_level VARCHAR(20) NOT NULL,
    notes TEXT,
    CONSTRAINT fk_skill_test_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    CONSTRAINT chk_score CHECK (score >= 0 AND score <= 100),
    CONSTRAINT chk_assigned_level CHECK (assigned_level IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED'))
);

-- Create Indexes for better performance
CREATE INDEX idx_students_email ON students(email);
CREATE INDEX idx_students_skill_level ON students(skill_level);
CREATE INDEX idx_instructors_email ON instructors(email);
CREATE INDEX idx_instructors_specialization ON instructors(specialization);
CREATE INDEX idx_courses_instructor ON courses(instructor_id);
CREATE INDEX idx_courses_term ON courses(term);
CREATE INDEX idx_courses_skill_level ON courses(skill_level);
CREATE INDEX idx_enrollments_student ON enrollments(student_id);
CREATE INDEX idx_enrollments_course ON enrollments(course_id);
CREATE INDEX idx_enrollments_status ON enrollments(status);
CREATE INDEX idx_sessions_course ON sessions(course_id);
CREATE INDEX idx_sessions_date ON sessions(session_date);
CREATE INDEX idx_attendance_enrollment ON attendance(enrollment_id);
CREATE INDEX idx_attendance_session ON attendance(session_id);
CREATE INDEX idx_payments_enrollment ON payments(enrollment_id);
CREATE INDEX idx_payments_status ON payments(status);
CREATE INDEX idx_skill_tests_student ON skill_tests(student_id);

-- Insert sample data (optional - for testing)
-- You can uncomment these if you want sample data

/*
-- Sample Instructor
INSERT INTO instructors (first_name, last_name, email, phone, specialization) 
VALUES ('John', 'Doe', 'john.doe@artschool.com', '555-0101', 'Painting');

-- Sample Student
INSERT INTO students (first_name, last_name, email, phone, date_of_birth, skill_level)
VALUES ('Jane', 'Smith', 'jane.smith@email.com', '555-0102', '1995-05-15', 'BEGINNER');

-- Sample Course
INSERT INTO courses (name, description, term, skill_level, instructor_id, max_capacity, fee, start_date, end_date)
VALUES ('Introduction to Painting', 'Learn the basics of painting', 'SUMMER', 'BEGINNER', 1, 20, 199.99, '2024-06-01', '2024-08-31');
*/

-- Grant permissions to user
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO art_school_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO art_school_user;

-- Success message
SELECT 'Database schema created successfully!' AS status;
