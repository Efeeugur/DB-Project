package model.dao;

import model.entity.Session;
import java.util.List;

/**
 * Data Access Object interface for Session entity.
 */
public interface SessionDAO extends GenericDAO<Session> {
    
    /**
     * Finds all sessions for a course.
     * @param courseId Course ID
     * @return List of sessions for the course
     */
    List<Session> findByCourseId(int courseId);
}
