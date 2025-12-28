package model.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents a course in the Art School system.
 */
public class Course {
    private int id;
    private String name;
    private String description;
    private Term term;
    private Student.SkillLevel skillLevel;
    private int instructorId;
    private int maxCapacity;
    private BigDecimal fee;
    private LocalDate startDate;
    private LocalDate endDate;

    public enum Term {
        SUMMER, WINTER
    }

    // Default constructor
    public Course() {
        this.maxCapacity = 20;
        this.fee = BigDecimal.ZERO;
    }

    // Parameterized constructor
    public Course(int id, String name, String description, Term term, 
                  Student.SkillLevel skillLevel, int instructorId, 
                  int maxCapacity, BigDecimal fee, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.term = term;
        this.skillLevel = skillLevel;
        this.instructorId = instructorId;
        this.maxCapacity = maxCapacity;
        this.fee = fee;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Term getTerm() { return term; }
    public void setTerm(Term term) { this.term = term; }

    public Student.SkillLevel getSkillLevel() { return skillLevel; }
    public void setSkillLevel(Student.SkillLevel skillLevel) { this.skillLevel = skillLevel; }

    public int getInstructorId() { return instructorId; }
    public void setInstructorId(int instructorId) { this.instructorId = instructorId; }

    public int getMaxCapacity() { return maxCapacity; }
    public void setMaxCapacity(int maxCapacity) { this.maxCapacity = maxCapacity; }

    public BigDecimal getFee() { return fee; }
    public void setFee(BigDecimal fee) { this.fee = fee; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    @Override
    public String toString() {
        return String.format("Course[id=%d, name=%s, term=%s, level=%s]",
                id, name, term, skillLevel);
    }
}
