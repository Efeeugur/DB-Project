package model.entity;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a weekly session/class in the Art School system.
 */
public class Session {
    private int id;
    private int courseId;
    private LocalDate sessionDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String topic;

    // Default constructor
    public Session() {}

    // Parameterized constructor
    public Session(int id, int courseId, LocalDate sessionDate, 
                   LocalTime startTime, LocalTime endTime, String topic) {
        this.id = id;
        this.courseId = courseId;
        this.sessionDate = sessionDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.topic = topic;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public LocalDate getSessionDate() { return sessionDate; }
    public void setSessionDate(LocalDate sessionDate) { this.sessionDate = sessionDate; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    @Override
    public String toString() {
        return String.format("Session[id=%d, courseId=%d, date=%s, topic=%s]",
                id, courseId, sessionDate, topic);
    }
}
