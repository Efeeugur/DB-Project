package model.dao.impl;

import model.dao.CourseDAO;
import model.dao.EnrollmentDAO;
import model.entity.Course;
import model.entity.Student;
import java.util.*;
import java.util.stream.Collectors;

/**
 * In-Memory implementation of CourseDAO.
 * Will be replaced with PostgreSQL implementation later.
 */
public class CourseDAOImpl implements CourseDAO {
    
    private final Map<Integer, Course> courses = new HashMap<>();
    private int nextId = 1;
    private EnrollmentDAO enrollmentDAO;
    
    public void setEnrollmentDAO(EnrollmentDAO enrollmentDAO) {
        this.enrollmentDAO = enrollmentDAO;
    }
    
    @Override
    public Course save(Course course) {
        course.setId(nextId++);
        courses.put(course.getId(), course);
        return course;
    }
    
    @Override
    public Optional<Course> findById(int id) {
        return Optional.ofNullable(courses.get(id));
    }
    
    @Override
    public List<Course> findAll() {
        return new ArrayList<>(courses.values());
    }
    
    @Override
    public Course update(Course course) {
        if (courses.containsKey(course.getId())) {
            courses.put(course.getId(), course);
            return course;
        }
        return null;
    }
    
    @Override
    public boolean delete(int id) {
        return courses.remove(id) != null;
    }
    
    @Override
    public int count() {
        return courses.size();
    }
    
    @Override
    public List<Course> findByTerm(Course.Term term) {
        return courses.values().stream()
                .filter(c -> c.getTerm() == term)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Course> findBySkillLevel(Student.SkillLevel level) {
        return courses.values().stream()
                .filter(c -> c.getSkillLevel() == level)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Course> findByInstructorId(int instructorId) {
        return courses.values().stream()
                .filter(c -> c.getInstructorId() == instructorId)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Course> searchByName(String name) {
        String lowerName = name.toLowerCase();
        return courses.values().stream()
                .filter(c -> c.getName().toLowerCase().contains(lowerName))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Course> findAvailableCourses() {
        if (enrollmentDAO == null) {
            return new ArrayList<>(courses.values());
        }
        return courses.values().stream()
                .filter(c -> enrollmentDAO.countByCourseId(c.getId()) < c.getMaxCapacity())
                .collect(Collectors.toList());
    }
}
