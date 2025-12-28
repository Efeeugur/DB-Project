package model.entity;

import java.time.LocalDateTime;

/**
 * Represents a student in the Art School system.
 */
public class Student {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String dateOfBirth;
    private SkillLevel skillLevel;
    private LocalDateTime createdAt;

    public enum SkillLevel {
        BEGINNER, INTERMEDIATE, ADVANCED
    }

    // Default constructor
    public Student() {
        this.createdAt = LocalDateTime.now();
        this.skillLevel = SkillLevel.BEGINNER;
    }

    // Parameterized constructor
    public Student(int id, String firstName, String lastName, String email, String phone, 
                   String dateOfBirth, SkillLevel skillLevel) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.skillLevel = skillLevel;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFullName() { return firstName + " " + lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public SkillLevel getSkillLevel() { return skillLevel; }
    public void setSkillLevel(SkillLevel skillLevel) { this.skillLevel = skillLevel; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return String.format("Student[id=%d, name=%s %s, email=%s, level=%s]",
                id, firstName, lastName, email, skillLevel);
    }
}
