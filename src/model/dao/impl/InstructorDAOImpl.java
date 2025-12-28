package model.dao.impl;

import model.dao.InstructorDAO;
import model.entity.Instructor;
import java.util.*;
import java.util.stream.Collectors;

/**
 * In-Memory implementation of InstructorDAO.
 * Will be replaced with PostgreSQL implementation later.
 */
public class InstructorDAOImpl implements InstructorDAO {
    
    private final Map<Integer, Instructor> instructors = new HashMap<>();
    private int nextId = 1;
    
    @Override
    public Instructor save(Instructor instructor) {
        instructor.setId(nextId++);
        instructors.put(instructor.getId(), instructor);
        return instructor;
    }
    
    @Override
    public Optional<Instructor> findById(int id) {
        return Optional.ofNullable(instructors.get(id));
    }
    
    @Override
    public List<Instructor> findAll() {
        return new ArrayList<>(instructors.values());
    }
    
    @Override
    public Instructor update(Instructor instructor) {
        if (instructors.containsKey(instructor.getId())) {
            instructors.put(instructor.getId(), instructor);
            return instructor;
        }
        return null;
    }
    
    @Override
    public boolean delete(int id) {
        return instructors.remove(id) != null;
    }
    
    @Override
    public int count() {
        return instructors.size();
    }
    
    @Override
    public Instructor findByEmail(String email) {
        return instructors.values().stream()
                .filter(i -> i.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public List<Instructor> findBySpecialization(String specialization) {
        return instructors.values().stream()
                .filter(i -> i.getSpecialization().equalsIgnoreCase(specialization))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<Instructor> searchByName(String name) {
        String lowerName = name.toLowerCase();
        return instructors.values().stream()
                .filter(i -> i.getFirstName().toLowerCase().contains(lowerName) ||
                            i.getLastName().toLowerCase().contains(lowerName))
                .collect(Collectors.toList());
    }
}
