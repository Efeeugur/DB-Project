package model.dao.impl;

import model.dao.EnrollmentDAO;
import model.entity.Enrollment;
import java.util.*;
import java.util.stream.Collectors;

/**
 * In-Memory implementation of EnrollmentDAO.
 * Will be replaced with PostgreSQL implementation later.
 */
public class EnrollmentDAOImpl implements EnrollmentDAO {
    
    private final Map<Integer, Enrollment> enrollments = new HashMap<>();
    private int nextId = 1;
    
    @Override
    public Enrollment save(Enrollment enrollment) {
        enrollment.setId(nextId++);
        enrollments.put(enrollment.getId(), enrollment);
        return enrollment;
    }
    
    @Override
    public Optional<Enrollment> findById(int id) {
        return Optional.ofNullable(enrollments.get(id));
    }
    
    @Override
    public List<Enrollment> findAll() {
        return new ArrayList<>(enrollments.values());
    }
    
    @Override
    public Enrollment update(Enrollment enrollment) {
        if (enrollments.containsKey(enrollment.getId())) {
            enrollments.put(enrollment.getId(), enrollment);
            return enrollment;
        }
        return null;
    }
    
    @Override
    public boolean delete(int id) {
        return enrollments.remove(id) != null;
    }
    
    @Override
    public int count() {
        return enrollments.size();
    }
    
    @Override
    public List<Enrollment> findByStudentId(int studentId) {
        return enrollments.values().stream()
                .filter(e -> e.getStudentId() == studentId)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Enrollment> findByCourseId(int courseId) {
        return enrollments.values().stream()
                .filter(e -> e.getCourseId() == courseId)
                .collect(Collectors.toList());
    }
    
    @Override
    public Enrollment findByStudentAndCourse(int studentId, int courseId) {
        return enrollments.values().stream()
                .filter(e -> e.getStudentId() == studentId && e.getCourseId() == courseId)
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public List<Enrollment> findActiveEnrollments() {
        return enrollments.values().stream()
                .filter(e -> e.getStatus() == Enrollment.Status.ACTIVE)
                .collect(Collectors.toList());
    }
    
    @Override
    public int countByCourseId(int courseId) {
        return (int) enrollments.values().stream()
                .filter(e -> e.getCourseId() == courseId && e.getStatus() == Enrollment.Status.ACTIVE)
                .count();
    }
}
