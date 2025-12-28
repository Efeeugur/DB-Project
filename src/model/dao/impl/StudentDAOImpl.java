package model.dao.impl;

import model.dao.StudentDAO;
import model.entity.Student;
import java.util.*;
import java.util.stream.Collectors;

/**
 * In-Memory implementation of StudentDAO.
 * Will be replaced with PostgreSQL implementation later.
 */
public class StudentDAOImpl implements StudentDAO {
    
    private final Map<Integer, Student> students = new HashMap<>();
    private int nextId = 1;
    
    @Override
    public Student save(Student student) {
        student.setId(nextId++);
        students.put(student.getId(), student);
        return student;
    }
    
    @Override
    public Optional<Student> findById(int id) {
        return Optional.ofNullable(students.get(id));
    }
    
    @Override
    public List<Student> findAll() {
        return new ArrayList<>(students.values());
    }
    
    @Override
    public Student update(Student student) {
        if (students.containsKey(student.getId())) {
            students.put(student.getId(), student);
            return student;
        }
        return null;
    }
    
    @Override
    public boolean delete(int id) {
        return students.remove(id) != null;
    }
    
    @Override
    public int count() {
        return students.size();
    }
    
    @Override
    public Student findByEmail(String email) {
        return students.values().stream()
                .filter(s -> s.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public List<Student> findBySkillLevel(Student.SkillLevel level) {
        return students.values().stream()
                .filter(s -> s.getSkillLevel() == level)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Student> searchByName(String name) {
        String lowerName = name.toLowerCase();
        return students.values().stream()
                .filter(s -> s.getFirstName().toLowerCase().contains(lowerName) ||
                            s.getLastName().toLowerCase().contains(lowerName))
                .collect(Collectors.toList());
    }
}
