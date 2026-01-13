package model.dao.postgresql;

import model.dao.SkillTestDAO;
import model.entity.SkillTest;
import model.entity.Student;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SkillTestDAOPostgreSQL implements SkillTestDAO {
    
    private final DatabaseConnection dbConnection;
    
    public SkillTestDAOPostgreSQL() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    @Override
    public SkillTest save(SkillTest skillTest) {
        String sql = "INSERT INTO skill_tests (student_id, score, assigned_level, notes) " +
                     "VALUES (?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, skillTest.getStudentId());
            stmt.setInt(2, skillTest.getScore());
            stmt.setString(3, skillTest.getAssignedLevel().toString());
            stmt.setString(4, skillTest.getNotes());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                skillTest.setId(rs.getInt("id"));
            }
            return skillTest;
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save skill test", e);
        }
    }
    
    @Override
    public SkillTest update(SkillTest skillTest) {
        String sql = "UPDATE skill_tests SET student_id = ?, score = ?, assigned_level = ?, notes = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, skillTest.getStudentId());
            stmt.setInt(2, skillTest.getScore());
            stmt.setString(3, skillTest.getAssignedLevel().toString());
            stmt.setString(4, skillTest.getNotes());
            stmt.setInt(5, skillTest.getId());
            
            stmt.executeUpdate();
            return skillTest;
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update skill test", e);
        }
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM skill_tests WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            return false;
        }
    }
    
    @Override
    public Optional<SkillTest> findById(int id) {
        String sql = "SELECT * FROM skill_tests WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToSkillTest(rs));
            }
            return Optional.empty();
            
        } catch (SQLException e) {
            return Optional.empty();
        }
    }
    
    @Override
    public List<SkillTest> findAll() {
        String sql = "SELECT * FROM skill_tests ORDER BY id";
        List<SkillTest> skillTests = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                skillTests.add(mapResultSetToSkillTest(rs));
            }
            return skillTests;
            
        } catch (SQLException e) {
            return skillTests;
        }
    }
    
    @Override
    public int count() {
        String sql = "SELECT COUNT(*) FROM skill_tests";
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
            
        } catch (SQLException e) {
            return 0;
        }
    }
    
    @Override
    public List<SkillTest> findByStudentId(int studentId) {
        String sql = "SELECT * FROM skill_tests WHERE student_id = ? ORDER BY test_date DESC";
        List<SkillTest> skillTests = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                skillTests.add(mapResultSetToSkillTest(rs));
            }
            return skillTests;
            
        } catch (SQLException e) {
            return skillTests;
        }
    }
    
    @Override
    public SkillTest findLatestByStudentId(int studentId) {
        String sql = "SELECT * FROM skill_tests WHERE student_id = ? ORDER BY test_date DESC LIMIT 1";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToSkillTest(rs);
            }
            return null;
            
        } catch (SQLException e) {
            return null;
        }
    }
    
    private SkillTest mapResultSetToSkillTest(ResultSet rs) throws SQLException {
        SkillTest skillTest = new SkillTest();
        skillTest.setId(rs.getInt("id"));
        skillTest.setStudentId(rs.getInt("student_id"));
        skillTest.setScore(rs.getInt("score"));
        skillTest.setAssignedLevel(Student.SkillLevel.valueOf(rs.getString("assigned_level")));
        skillTest.setNotes(rs.getString("notes"));
        
        Timestamp testDate = rs.getTimestamp("test_date");
        if (testDate != null) {
            skillTest.setTestDate(testDate.toLocalDateTime());
        }
        
        return skillTest;
    }
}
