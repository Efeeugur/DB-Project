package model.dao;

import model.entity.Student;
import java.util.List;

/**
 * Data Access Object interface for Student entity.
 */
public interface StudentDAO extends GenericDAO<Student> {
    
    /**
     * Finds a student by email.
     * @param email Student email
     * @return Student if found, null otherwise
     */
    Student findByEmail(String email);
    
    /**
     * Finds all students by skill level.
     * @param level Skill level
     * @return List of students with the given level
     */
    List<Student> findBySkillLevel(Student.SkillLevel level);
    
    /**
     * Searches students by name.
     * @param name Name to search for
     * @return List of matching students
     */
    List<Student> searchByName(String name);
}
