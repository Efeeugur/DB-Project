package model.entity;

/**
 * Represents attendance record for a session.
 */
public class Attendance {
    private int id;
    private int enrollmentId;
    private int sessionId;
    private AttendanceStatus status;
    private String notes;

    public enum AttendanceStatus {
        PRESENT, ABSENT, LATE
    }

    // Default constructor
    public Attendance() {
        this.status = AttendanceStatus.PRESENT;
    }

    // Parameterized constructor
    public Attendance(int id, int enrollmentId, int sessionId, 
                      AttendanceStatus status, String notes) {
        this.id = id;
        this.enrollmentId = enrollmentId;
        this.sessionId = sessionId;
        this.status = status;
        this.notes = notes;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEnrollmentId() { return enrollmentId; }
    public void setEnrollmentId(int enrollmentId) { this.enrollmentId = enrollmentId; }

    public int getSessionId() { return sessionId; }
    public void setSessionId(int sessionId) { this.sessionId = sessionId; }

    public AttendanceStatus getStatus() { return status; }
    public void setStatus(AttendanceStatus status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return String.format("Attendance[id=%d, enrollmentId=%d, sessionId=%d, status=%s]",
                id, enrollmentId, sessionId, status);
    }
}
