package model.entity;

import java.time.LocalDateTime;

/**
 * Represents a skill assessment test for student placement.
 */
public class SkillTest {
    private int id;
    private int studentId;
    private LocalDateTime testDate;
    private int score;
    private Student.SkillLevel assignedLevel;
    private String notes;

    // Default constructor
    public SkillTest() {
        this.testDate = LocalDateTime.now();
    }

    // Parameterized constructor
    public SkillTest(int id, int studentId, int score, 
                     Student.SkillLevel assignedLevel, String notes) {
        this.id = id;
        this.studentId = studentId;
        this.testDate = LocalDateTime.now();
        this.score = score;
        this.assignedLevel = assignedLevel;
        this.notes = notes;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public LocalDateTime getTestDate() { return testDate; }
    public void setTestDate(LocalDateTime testDate) { this.testDate = testDate; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public Student.SkillLevel getAssignedLevel() { return assignedLevel; }
    public void setAssignedLevel(Student.SkillLevel assignedLevel) { this.assignedLevel = assignedLevel; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    /**
     * Determines skill level based on test score.
     * 0-40: BEGINNER, 41-70: INTERMEDIATE, 71-100: ADVANCED
     */
    public static Student.SkillLevel determineLevel(int score) {
        if (score <= 40) {
            return Student.SkillLevel.BEGINNER;
        } else if (score <= 70) {
            return Student.SkillLevel.INTERMEDIATE;
        } else {
            return Student.SkillLevel.ADVANCED;
        }
    }

    @Override
    public String toString() {
        return String.format("SkillTest[id=%d, studentId=%d, score=%d, level=%s]",
                id, studentId, score, assignedLevel);
    }
}
