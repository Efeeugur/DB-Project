package model.dao.impl;

import model.dao.AttendanceDAO;
import model.entity.Attendance;
import java.util.*;
import java.util.stream.Collectors;

/**
 * In-Memory implementation of AttendanceDAO.
 * Will be replaced with PostgreSQL implementation later.
 */
public class AttendanceDAOImpl implements AttendanceDAO {
    
    private final Map<Integer, Attendance> attendances = new HashMap<>();
    private int nextId = 1;
    
    @Override
    public Attendance save(Attendance attendance) {
        attendance.setId(nextId++);
        attendances.put(attendance.getId(), attendance);
        return attendance;
    }
    
    @Override
    public Optional<Attendance> findById(int id) {
        return Optional.ofNullable(attendances.get(id));
    }
    
    @Override
    public List<Attendance> findAll() {
        return new ArrayList<>(attendances.values());
    }
    
    @Override
    public Attendance update(Attendance attendance) {
        if (attendances.containsKey(attendance.getId())) {
            attendances.put(attendance.getId(), attendance);
            return attendance;
        }
        return null;
    }
    
    @Override
    public boolean delete(int id) {
        return attendances.remove(id) != null;
    }
    
    @Override
    public int count() {
        return attendances.size();
    }
    
    @Override
    public List<Attendance> findByEnrollmentId(int enrollmentId) {
        return attendances.values().stream()
                .filter(a -> a.getEnrollmentId() == enrollmentId)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Attendance> findBySessionId(int sessionId) {
        return attendances.values().stream()
                .filter(a -> a.getSessionId() == sessionId)
                .collect(Collectors.toList());
    }
    
    @Override
    public Attendance findByEnrollmentAndSession(int enrollmentId, int sessionId) {
        return attendances.values().stream()
                .filter(a -> a.getEnrollmentId() == enrollmentId && a.getSessionId() == sessionId)
                .findFirst()
                .orElse(null);
    }
}
