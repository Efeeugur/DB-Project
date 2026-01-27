# Art School Management System

A professional desktop application developed using Java Swing and PostgreSQL to manage the daily operations of an art school.
The system covers the full lifecycle of school management including student registration, course scheduling, enrollment control, attendance tracking, and financial processing.
This project is designed with a strong focus on database design, business logic enforcement, and clean software architecture.

## Table of Contents
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation and Setup](#installation-and-setup)
- [Building and Running](#building-and-running)
- [Usage Guide](#usage-guide)
- [Architecture and Design](#architecture-and-design)
- [Troubleshooting](#troubleshooting)
- [Team](#team)

## Architecture

```
src/
├── Main.java                 # Application entry point
├── controller/               # Business logic (MVC Controller)
├── model/
│   ├── entity/              # Data models (POJOs)
│   └── dao/                 # Data Access Objects
│       └── impl/            # In-Memory implementations
├── view/                    # Swing GUI panels
└── util/                    # Utility classes
```
## Features

### Student Management
- Register students with detailed personal and contact information
- Assign and manage student skill levels (Beginner, Intermediate, Advanced)
- View enrollment history and past course participation

### Course Management
- Create courses with:
  - Skill prerequisites
  - Capacity limits
  - Tuition fees
- Assign instructors to courses
- Organize courses by academic terms (e.g. Spring 2024)

### Enrollment and Attendance
- **Skill-based enrollment validation**: Beginner students cannot enroll in Advanced courses
- **Capacity checks**: Prevent over-enrollment
- **Attendance tracking** with status types:
  - Present
  - Absent
  - Late
  - Excused

### Financial Management
- Automatic tuition record creation upon enrollment
- Payment tracking with multiple payment methods:
  - Cash
  - Card
  - Bank Transfer
- Refund handling for dropped courses

## Prerequisites

Ensure the following software is installed on your system:

**1. Java Development Kit (JDK) 17 or higher**
- Verify installation:
  ```bash
  java -version
  ```

**2. PostgreSQL 14 or higher**
- Verify installation:
  ```bash
  psql --version
  ```

**3. Git**
- Verify installation:
  ```bash
  git --version
  ```

## Installation and Setup

### 1. Clone the Repository
```bash
git clone https://github.com/Efeeugur/DB-Project.git
cd DB-Project
```

### 2. Database Configuration

#### 2.1 Login to PostgreSQL
```bash
psql -U postgres
```

#### 2.2 Create User and Database
Run the following SQL commands:
```sql
CREATE USER art_school_user WITH PASSWORD 'your_secure_password';
CREATE DATABASE "DB-Project";
GRANT ALL PRIVILEGES ON DATABASE "DB-Project" TO art_school_user;
\c "DB-Project"
GRANT ALL ON SCHEMA public TO art_school_user;
```
Exit PostgreSQL:
```bash
\q
```

#### 2.3 Create Database Schema
```bash
psql -U art_school_user -h localhost -d "DB-Project" -f schema.sql
```

#### 2.4 Insert Sample Data (Optional)
```bash
psql -U art_school_user -h localhost -d "DB-Project" -f populate_test_data.sql
```

### Dependencies (Manual Setup)
*If Maven is not used, required libraries must be added manually.*

1. **Create output directory:**
   ```bash
   mkdir -p out
   ```
2. **FlatLaf UI Library:**
   Download `flatlaf-3.4.jar` and place it in the `out/` directory.

3. **PostgreSQL JDBC Driver:**
   Ensure `postgresql.jar` (42.x.x) is available in the project root or `out/` directory.

## Building and Running

### Option A: Using Maven
```bash
mvn clean compile
mvn exec:java
```

### Option B: Without Maven

**MacOS / Linux:**
```bash
javac -cp "out/flatlaf.jar:postgresql.jar" -d out $(find src -name "*.java")
java -cp "out:out/postgresql.jar:out/flatlaf.jar" Main
```

**Windows (PowerShell):**
```powershell
javac -cp "out/flatlaf.jar;postgresql.jar" -d out (Get-ChildItem src -Recurse -Filter *.java).FullName
java -cp "out;out/postgresql.jar;out/flatlaf.jar" Main
```

## Usage Guide

### Dashboard
- Displays total counts of students, instructors, active enrollments, and pending payments
- Sidebar navigation for quick access to all modules

### Registering a Student
1. Open the **Students** module
2. Click **"Add Student"**
3. Enter student details
4. Assign an initial skill level
5. Save the record

### Creating a Course
1. Open the **Courses** module
2. Click **"Add Course"**
3. Set prerequisite skill level and capacity
4. Assign an instructor and tuition fee
5. Save the course

### Enrolling a Student
1. Open the **Enrollments** module
2. Click **"New Enrollment"**
3. Select a student and a course
4. System validates skill level and capacity
5. Enrollment confirmation automatically creates a payment record

## Architecture and Design
The application follows the **Model-View-Controller (MVC)** architecture.

### Model Layer
- Entity classes representing database tables
- DAO interfaces defining database operations
- DAO implementations using `PreparedStatement` to prevent SQL injection

### View Layer
- Java Swing components
- Layout managers such as `GridBagLayout` and `BorderLayout`
- FlatLaf used for modern UI styling

### Controller Layer
- Handles communication between View and Model
- Implements business rules such as enrollment validation and capacity checks

## Troubleshooting

**Error: Relation does not exist**
> Ensure `schema.sql` has been executed successfully.

**Error: ClassNotFoundException (FlatLaf)**
> Verify `flatlaf.jar` is included in the classpath.

**Error: Password authentication failed**
> Check database credentials in `DBConnection.java`.

**UI appears outdated**
> FlatLaf failed to load. Check console logs for LookAndFeel errors.

## Team Members

| Student ID | Name |
|------------|------|
| 64210015 | Nurefşan Ergören |
| 64210039 | Efe Uğur |
| 64220034 | Mustafa Alperen Erçevik |
