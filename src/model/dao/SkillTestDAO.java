package model.dao;

import model.entity.SkillTest;
import java.util.List;

/**
 * Data Access Object interface for SkillTest entity.
 */
public interface SkillTestDAO extends GenericDAO<SkillTest> {
    
    /**
     * Finds all skill tests for a student.
     * @param studentId Student ID
     * @return List of skill tests
     */
    List<SkillTest> findByStudentId(int studentId);
    
    /**
     * Finds the latest skill test for a student.
     * @param studentId Student ID
     * @return Latest skill test if exists
     */
    SkillTest findLatestByStudentId(int studentId);
}
