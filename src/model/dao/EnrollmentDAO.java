package model.dao;

import model.entity.Enrollment;
import java.util.List;

/**
 * Data Access Object interface for Enrollment entity.
 */
public interface EnrollmentDAO extends GenericDAO<Enrollment> {
    
    /**
     * Finds all enrollments for a student.
     * @param studentId Student ID
     * @return List of enrollments for the student
     */
    List<Enrollment> findByStudentId(int studentId);
    
    /**
     * Finds all enrollments for a course.
     * @param courseId Course ID
     * @return List of enrollments for the course
     */
    List<Enrollment> findByCourseId(int courseId);
    
    /**
     * Finds enrollment by student and course.
     * @param studentId Student ID
     * @param courseId Course ID
     * @return Enrollment if exists, null otherwise
     */
    Enrollment findByStudentAndCourse(int studentId, int courseId);
    
    /**
     * Finds all active enrollments.
     * @return List of active enrollments
     */
    List<Enrollment> findActiveEnrollments();
    
    /**
     * Counts enrollments for a course.
     * @param courseId Course ID
     * @return Number of enrollments
     */
    int countByCourseId(int courseId);
}
