package controller;

import model.dao.InstructorDAO;
import model.entity.Instructor;
import java.util.List;
import java.util.Optional;

/**
 * Controller for Instructor operations.
 */
public class InstructorController {
    
    private final InstructorDAO instructorDAO;
    
    public InstructorController(InstructorDAO instructorDAO) {
        this.instructorDAO = instructorDAO;
    }
    
    /**
     * Registers a new instructor.
     */
    public Instructor registerInstructor(String firstName, String lastName, String email, 
                                         String phone, String specialization) {
        // Check if email already exists
        if (instructorDAO.findByEmail(email) != null) {
            throw new IllegalArgumentException("An instructor with this email already exists.");
        }
        
        Instructor instructor = new Instructor();
        instructor.setFirstName(firstName);
        instructor.setLastName(lastName);
        instructor.setEmail(email);
        instructor.setPhone(phone);
        instructor.setSpecialization(specialization);
        
        return instructorDAO.save(instructor);
    }
    
    /**
     * Gets an instructor by ID.
     */
    public Optional<Instructor> getInstructorById(int id) {
        return instructorDAO.findById(id);
    }
    
    /**
     * Gets all instructors.
     */
    public List<Instructor> getAllInstructors() {
        return instructorDAO.findAll();
    }
    
    /**
     * Updates instructor information.
     */
    public Instructor updateInstructor(Instructor instructor) {
        return instructorDAO.update(instructor);
    }
    
    /**
     * Deletes an instructor.
     */
    public boolean deleteInstructor(int id) {
        return instructorDAO.delete(id);
    }
    
    /**
     * Gets instructors by specialization.
     */
    public List<Instructor> getInstructorsBySpecialization(String specialization) {
        return instructorDAO.findBySpecialization(specialization);
    }
    
    /**
     * Searches instructors by name.
     */
    public List<Instructor> searchInstructors(String name) {
        return instructorDAO.searchByName(name);
    }
    
    /**
     * Gets total instructor count.
     */
    public int getInstructorCount() {
        return instructorDAO.count();
    }
}
