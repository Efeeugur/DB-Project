package model.dao;

import model.entity.Course;
import model.entity.Student;
import java.util.List;

/**
 * Data Access Object interface for Course entity.
 */
public interface CourseDAO extends GenericDAO<Course> {
    
    /**
     * Finds all courses by term.
     * @param term Course term (SUMMER/WINTER)
     * @return List of courses for the given term
     */
    List<Course> findByTerm(Course.Term term);
    
    /**
     * Finds all courses by skill level.
     * @param level Skill level
     * @return List of courses for the given level
     */
    List<Course> findBySkillLevel(Student.SkillLevel level);
    
    /**
     * Finds all courses by instructor.
     * @param instructorId Instructor ID
     * @return List of courses taught by the instructor
     */
    List<Course> findByInstructorId(int instructorId);
    
    /**
     * Searches courses by name.
     * @param name Name to search for
     * @return List of matching courses
     */
    List<Course> searchByName(String name);
    
    /**
     * Finds courses with available capacity.
     * @return List of courses that are not full
     */
    List<Course> findAvailableCourses();
}
