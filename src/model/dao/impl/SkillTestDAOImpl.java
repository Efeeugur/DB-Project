package model.dao.impl;

import model.dao.SkillTestDAO;
import model.entity.SkillTest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * In-Memory implementation of SkillTestDAO.
 * Will be replaced with PostgreSQL implementation later.
 */
public class SkillTestDAOImpl implements SkillTestDAO {
    
    private final Map<Integer, SkillTest> skillTests = new HashMap<>();
    private int nextId = 1;
    
    @Override
    public SkillTest save(SkillTest skillTest) {
        skillTest.setId(nextId++);
        skillTests.put(skillTest.getId(), skillTest);
        return skillTest;
    }
    
    @Override
    public Optional<SkillTest> findById(int id) {
        return Optional.ofNullable(skillTests.get(id));
    }
    
    @Override
    public List<SkillTest> findAll() {
        return new ArrayList<>(skillTests.values());
    }
    
    @Override
    public SkillTest update(SkillTest skillTest) {
        if (skillTests.containsKey(skillTest.getId())) {
            skillTests.put(skillTest.getId(), skillTest);
            return skillTest;
        }
        return null;
    }
    
    @Override
    public boolean delete(int id) {
        return skillTests.remove(id) != null;
    }
    
    @Override
    public int count() {
        return skillTests.size();
    }
    
    @Override
    public List<SkillTest> findByStudentId(int studentId) {
        return skillTests.values().stream()
                .filter(t -> t.getStudentId() == studentId)
                .collect(Collectors.toList());
    }
    
    @Override
    public SkillTest findLatestByStudentId(int studentId) {
        return skillTests.values().stream()
                .filter(t -> t.getStudentId() == studentId)
                .max(Comparator.comparing(SkillTest::getTestDate))
                .orElse(null);
    }
}
