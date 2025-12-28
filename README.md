# 🎨 Art School Management System

A Java Swing desktop application for managing art school operations including students, instructors, courses, enrollments, attendance, and payments.

![Java](https://img.shields.io/badge/Java-17%2B-orange)
![Swing](https://img.shields.io/badge/GUI-Swing-blue)
![MVC](https://img.shields.io/badge/Architecture-MVC-green)
![License](https://img.shields.io/badge/License-MIT-yellow)

## 📋 Features

- **Student Management**: Register, update, delete students with skill level tracking
- **Instructor Management**: Manage instructors with specialization fields
- **Course Management**: Create seasonal courses (Summer/Winter) with skill levels
- **Enrollment System**: Enroll students in matching skill-level courses
- **Attendance Tracking**: Record and calculate attendance percentages
- **Payment Management**: Track and process course payments
- **Skill Assessment**: Conduct skill tests to determine student levels
- **Dashboard**: Overview of system statistics
- **Reports**: Generate summary reports

## 🏗️ Architecture

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

## 🚀 Getting Started

### Prerequisites

- Java JDK 17 or higher
- Maven 3.6+ (optional, for dependency management)

### Installation

1. Clone the repository:
```bash
git clone https://github.com/Efeeugur/DB-Project.git
cd DB-Project
```

2. Compile the project:
```bash
# Using Maven
mvn clean compile

# Or manually
mkdir -p out
javac -d out $(find src -name "*.java")
```

3. Run the application:
```bash
# Using Maven
mvn exec:java

# Or manually
java -cp out Main
```

## 📸 Screenshots

### Dashboard
The main dashboard displays key statistics including total students, instructors, courses, active enrollments, and pending payments.

### Student Management
Full CRUD operations with skill test functionality to assess and assign student levels.

### Course Management
Create and manage seasonal art courses with instructor assignments.

## 👥 Team Members

| Student ID | Name |
|------------|------|
| 64210015 | Nurefşan Ergören |
| 64210039 | Efe Uğur |
| 64220034 | Mustafa Alperen Erçevik |

## 🛠️ Technologies Used

- **Language**: Java 17
- **GUI**: Java Swing
- **Architecture**: MVC (Model-View-Controller)
- **Build Tool**: Maven
- **Database**: In-Memory (PostgreSQL ready)

## 📁 Project Structure

| Package | Description |
|---------|-------------|
| `controller` | Business logic and validation |
| `model.entity` | Data models (Student, Instructor, Course, etc.) |
| `model.dao` | Data Access Object interfaces |
| `model.dao.impl` | In-Memory DAO implementations |
| `view` | Swing GUI panels and frames |
| `util` | Helper classes (SwingUtils, GlobalExceptionHandler, etc.) |

## 🔮 Future Improvements

- [ ] PostgreSQL database integration
- [ ] PDF/Excel export functionality
- [ ] Multi-language support
- [ ] User authentication system
- [ ] Backup and restore feature

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request
