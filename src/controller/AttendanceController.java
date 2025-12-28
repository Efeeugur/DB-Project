package controller;

import model.dao.AttendanceDAO;
import model.dao.EnrollmentDAO;
import model.dao.SessionDAO;
import model.entity.Attendance;
import model.entity.Enrollment;
import model.entity.Session;
import java.util.List;
import java.util.Optional;

/**
 * Controller for Attendance operations.
 */
public class AttendanceController {
    
    private final AttendanceDAO attendanceDAO;
    private final EnrollmentDAO enrollmentDAO;
    private final SessionDAO sessionDAO;
    
    public AttendanceController(AttendanceDAO attendanceDAO, EnrollmentDAO enrollmentDAO, 
                                SessionDAO sessionDAO) {
        this.attendanceDAO = attendanceDAO;
        this.enrollmentDAO = enrollmentDAO;
        this.sessionDAO = sessionDAO;
    }
    
    /**
     * Records attendance for a student in a session.
     */
    public Attendance recordAttendance(int enrollmentId, int sessionId, 
                                       Attendance.AttendanceStatus status, String notes) {
        // Validate enrollment exists
        Optional<Enrollment> enrollmentOpt = enrollmentDAO.findById(enrollmentId);
        if (enrollmentOpt.isEmpty()) {
            throw new IllegalArgumentException("Enrollment not found.");
        }
        
        // Validate session exists
        Optional<Session> sessionOpt = sessionDAO.findById(sessionId);
        if (sessionOpt.isEmpty()) {
            throw new IllegalArgumentException("Session not found.");
        }
        
        // Check if attendance already recorded
        Attendance existing = attendanceDAO.findByEnrollmentAndSession(enrollmentId, sessionId);
        if (existing != null) {
            // Update existing attendance
            existing.setStatus(status);
            existing.setNotes(notes);
            return attendanceDAO.update(existing);
        }
        
        // Create new attendance record
        Attendance attendance = new Attendance();
        attendance.setEnrollmentId(enrollmentId);
        attendance.setSessionId(sessionId);
        attendance.setStatus(status);
        attendance.setNotes(notes);
        
        return attendanceDAO.save(attendance);
    }
    
    /**
     * Gets attendance for a session.
     */
    public List<Attendance> getSessionAttendance(int sessionId) {
        return attendanceDAO.findBySessionId(sessionId);
    }
    
    /**
     * Gets attendance for an enrollment.
     */
    public List<Attendance> getEnrollmentAttendance(int enrollmentId) {
        return attendanceDAO.findByEnrollmentId(enrollmentId);
    }
    
    /**
     * Calculates attendance percentage for an enrollment.
     */
    public double calculateAttendancePercentage(int enrollmentId) {
        List<Attendance> attendances = attendanceDAO.findByEnrollmentId(enrollmentId);
        if (attendances.isEmpty()) {
            return 0.0;
        }
        
        long presentCount = attendances.stream()
                .filter(a -> a.getStatus() == Attendance.AttendanceStatus.PRESENT ||
                            a.getStatus() == Attendance.AttendanceStatus.LATE)
                .count();
        
        return (double) presentCount / attendances.size() * 100;
    }
}
