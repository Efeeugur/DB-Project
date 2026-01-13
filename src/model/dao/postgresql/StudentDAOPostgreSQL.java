package model.dao.postgresql;

import model.dao.StudentDAO;
import model.entity.Student;
import util.DatabaseConnection;
import util.AppLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * PostgreSQL implementation of StudentDAO.
 */
public class StudentDAOPostgreSQL implements StudentDAO {
    
    private final DatabaseConnection dbConnection;
    
    public StudentDAOPostgreSQL() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    @Override
    public Student save(Student student) {
        String sql = "INSERT INTO students (first_name, last_name, email, phone, date_of_birth, skill_level) " +
                     "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, student.getPhone());
            stmt.setString(5, student.getDateOfBirth());
            stmt.setString(6, student.getSkillLevel().toString());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                student.setId(rs.getInt("id"));
            }
            
            AppLogger.info("Student saved: " + student.getEmail());
            return student;
            
        } catch (SQLException e) {
            AppLogger.error("Error saving student: " + e.getMessage());
            throw new RuntimeException("Failed to save student", e);
        }
    }
    
    @Override
    public Student update(Student student) {
        String sql = "UPDATE students SET first_name = ?, last_name = ?, email = ?, " +
                     "phone = ?, date_of_birth = ?, skill_level = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, student.getPhone());
            stmt.setString(5, student.getDateOfBirth());
            stmt.setString(6, student.getSkillLevel().toString());
            stmt.setInt(7, student.getId());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected == 0) {
                throw new RuntimeException("Student not found: " + student.getId());
            }
            
            AppLogger.info("Student updated: " + student.getId());
            return student;
            
        } catch (SQLException e) {
            AppLogger.error("Error updating student: " + e.getMessage());
            throw new RuntimeException("Failed to update student", e);
        }
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM students WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                AppLogger.info("Student deleted: " + id);
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            AppLogger.error("Error deleting student: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public Optional<Student> findById(int id) {
        String sql = "SELECT * FROM students WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToStudent(rs));
            }
            return Optional.empty();
            
        } catch (SQLException e) {
            AppLogger.error("Error finding student by ID: " + e.getMessage());
            return Optional.empty();
        }
    }
    
    @Override
    public List<Student> findAll() {
        String sql = "SELECT * FROM students ORDER BY id";
        List<Student> students = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
            
            AppLogger.debug("Found " + students.size() + " students");
            return students;
            
        } catch (SQLException e) {
            AppLogger.error("Error finding all students: " + e.getMessage());
            return students;
        }
    }
    
    @Override
    public int count() {
        String sql = "SELECT COUNT(*) FROM students";
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
            
        } catch (SQLException e) {
            AppLogger.error("Error counting students: " + e.getMessage());
            return 0;
        }
    }
    
    @Override
    public Student findByEmail(String email) {
        String sql = "SELECT * FROM students WHERE email = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToStudent(rs);
            }
            return null;
            
        } catch (SQLException e) {
            AppLogger.error("Error finding student by email: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public List<Student> findBySkillLevel(Student.SkillLevel level) {
        String sql = "SELECT * FROM students WHERE skill_level = ? ORDER BY id";
        List<Student> students = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, level.toString());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
            
            return students;
            
        } catch (SQLException e) {
            AppLogger.error("Error finding students by skill level: " + e.getMessage());
            return students;
        }
    }
    
    @Override
    public List<Student> searchByName(String name) {
        String sql = "SELECT * FROM students WHERE " +
                     "LOWER(first_name) LIKE LOWER(?) OR LOWER(last_name) LIKE LOWER(?) " +
                     "ORDER BY first_name, last_name";
        List<Student> students = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + name + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
            
            return students;
            
        } catch (SQLException e) {
            AppLogger.error("Error searching students by name: " + e.getMessage());
            return students;
        }
    }
    
    /**
     * Maps ResultSet row to Student object.
     */
    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setId(rs.getInt("id"));
        student.setFirstName(rs.getString("first_name"));
        student.setLastName(rs.getString("last_name"));
        student.setEmail(rs.getString("email"));
        student.setPhone(rs.getString("phone"));
        student.setDateOfBirth(rs.getString("date_of_birth"));
        student.setSkillLevel(Student.SkillLevel.valueOf(rs.getString("skill_level")));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            student.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return student;
    }
}
