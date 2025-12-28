package model.entity;

import java.time.LocalDateTime;

/**
 * Represents an instructor in the Art School system.
 */
public class Instructor {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String specialization;
    private LocalDateTime createdAt;

    // Default constructor
    public Instructor() {
        this.createdAt = LocalDateTime.now();
    }

    // Parameterized constructor
    public Instructor(int id, String firstName, String lastName, String email, 
                      String phone, String specialization) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.specialization = specialization;
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

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return String.format("Instructor[id=%d, name=%s %s, specialization=%s]",
                id, firstName, lastName, specialization);
    }
}
