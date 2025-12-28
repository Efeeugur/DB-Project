package model.dao;

import model.entity.Instructor;
import java.util.List;

/**
 * Data Access Object interface for Instructor entity.
 */
public interface InstructorDAO extends GenericDAO<Instructor> {
    
    /**
     * Finds an instructor by email.
     * @param email Instructor email
     * @return Instructor if found, null otherwise
     */
    Instructor findByEmail(String email);
    
    /**
     * Finds all instructors by specialization.
     * @param specialization Specialization to filter by
     * @return List of instructors with the given specialization
     */
    List<Instructor> findBySpecialization(String specialization);
    
    /**
     * Searches instructors by name.
     * @param name Name to search for
     * @return List of matching instructors
     */
    List<Instructor> searchByName(String name);
}
