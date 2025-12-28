package model.entity;

import java.time.LocalDateTime;

/**
 * Represents a student enrollment in a course.
 */
public class Enrollment {
    private int id;
    private int studentId;
    private int courseId;
    private LocalDateTime enrollmentDate;
    private Status status;

    public enum Status {
        ACTIVE, COMPLETED, DROPPED
    }

    // Default constructor
    public Enrollment() {
        this.enrollmentDate = LocalDateTime.now();
        this.status = Status.ACTIVE;
    }

    // Parameterized constructor
    public Enrollment(int id, int studentId, int courseId, Status status) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrollmentDate = LocalDateTime.now();
        this.status = status;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public LocalDateTime getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDateTime enrollmentDate) { this.enrollmentDate = enrollmentDate; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("Enrollment[id=%d, studentId=%d, courseId=%d, status=%s]",
                id, studentId, courseId, status);
    }
}
