package model.dao;

import model.entity.Attendance;
import java.util.List;

/**
 * Data Access Object interface for Attendance entity.
 */
public interface AttendanceDAO extends GenericDAO<Attendance> {
    
    /**
     * Finds all attendance records for an enrollment.
     * @param enrollmentId Enrollment ID
     * @return List of attendance records
     */
    List<Attendance> findByEnrollmentId(int enrollmentId);
    
    /**
     * Finds all attendance records for a session.
     * @param sessionId Session ID
     * @return List of attendance records
     */
    List<Attendance> findBySessionId(int sessionId);
    
    /**
     * Finds attendance by enrollment and session.
     * @param enrollmentId Enrollment ID
     * @param sessionId Session ID
     * @return Attendance record if exists
     */
    Attendance findByEnrollmentAndSession(int enrollmentId, int sessionId);
}
