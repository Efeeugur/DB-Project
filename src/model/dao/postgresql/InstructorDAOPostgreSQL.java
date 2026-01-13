package model.dao.postgresql;

import model.dao.InstructorDAO;
import model.entity.Instructor;
import util.DatabaseConnection;
import util.AppLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * PostgreSQL implementation of InstructorDAO.
 */
public class InstructorDAOPostgreSQL implements InstructorDAO {
    
    private final DatabaseConnection dbConnection;
    
    public InstructorDAOPostgreSQL() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    @Override
    public Instructor save(Instructor instructor) {
        String sql = "INSERT INTO instructors (first_name, last_name, email, phone, specialization) " +
                     "VALUES (?, ?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, instructor.getFirstName());
            stmt.setString(2, instructor.getLastName());
            stmt.setString(3, instructor.getEmail());
            stmt.setString(4, instructor.getPhone());
            stmt.setString(5, instructor.getSpecialization());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                instructor.setId(rs.getInt("id"));
            }
            
            AppLogger.info("Instructor saved: " + instructor.getEmail());
            return instructor;
            
        } catch (SQLException e) {
            AppLogger.error("Error saving instructor: " + e.getMessage());
            throw new RuntimeException("Failed to save instructor", e);
        }
    }
    
    @Override
    public Instructor update(Instructor instructor) {
        String sql = "UPDATE instructors SET first_name = ?, last_name = ?, email = ?, " +
                     "phone = ?, specialization = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, instructor.getFirstName());
            stmt.setString(2, instructor.getLastName());
            stmt.setString(3, instructor.getEmail());
            stmt.setString(4, instructor.getPhone());
            stmt.setString(5, instructor.getSpecialization());
            stmt.setInt(6, instructor.getId());
            
            stmt.executeUpdate();
            AppLogger.info("Instructor updated: " + instructor.getId());
            return instructor;
            
        } catch (SQLException e) {
            AppLogger.error("Error updating instructor: " + e.getMessage());
            throw new RuntimeException("Failed to update instructor", e);
        }
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM instructors WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                AppLogger.info("Instructor deleted: " + id);
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            AppLogger.error("Error deleting instructor: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public Optional<Instructor> findById(int id) {
        String sql = "SELECT * FROM instructors WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToInstructor(rs));
            }
            return Optional.empty();
            
        } catch (SQLException e) {
            AppLogger.error("Error finding instructor by ID: " + e.getMessage());
            return Optional.empty();
        }
    }
    
    @Override
    public List<Instructor> findAll() {
        String sql = "SELECT * FROM instructors ORDER BY id";
        List<Instructor> instructors = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                instructors.add(mapResultSetToInstructor(rs));
            }
            
            return instructors;
            
        } catch (SQLException e) {
            AppLogger.error("Error finding all instructors: " + e.getMessage());
            return instructors;
        }
    }
    
    @Override
    public int count() {
        String sql = "SELECT COUNT(*) FROM instructors";
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
            
        } catch (SQLException e) {
            AppLogger.error("Error counting instructors: " + e.getMessage());
            return 0;
        }
    }
    
    @Override
    public Instructor findByEmail(String email) {
        String sql = "SELECT * FROM instructors WHERE email = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToInstructor(rs);
            }
            return null;
            
        } catch (SQLException e) {
            AppLogger.error("Error finding instructor by email: " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public List<Instructor> findBySpecialization(String specialization) {
        String sql = "SELECT * FROM instructors WHERE LOWER(specialization) LIKE LOWER(?) ORDER BY id";
        List<Instructor> instructors = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + specialization + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                instructors.add(mapResultSetToInstructor(rs));
            }
            
            return instructors;
            
        } catch (SQLException e) {
            AppLogger.error("Error finding instructors by specialization: " + e.getMessage());
            return instructors;
        }
    }
    
    @Override
    public List<Instructor> searchByName(String name) {
        String sql = "SELECT * FROM instructors WHERE " +
                     "LOWER(first_name) LIKE LOWER(?) OR LOWER(last_name) LIKE LOWER(?) " +
                     "ORDER BY first_name, last_name";
        List<Instructor> instructors = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + name + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                instructors.add(mapResultSetToInstructor(rs));
            }
            
            return instructors;
            
        } catch (SQLException e) {
            AppLogger.error("Error searching instructors by name: " + e.getMessage());
            return instructors;
        }
    }
    
    private Instructor mapResultSetToInstructor(ResultSet rs) throws SQLException {
        Instructor instructor = new Instructor();
        instructor.setId(rs.getInt("id"));
        instructor.setFirstName(rs.getString("first_name"));
        instructor.setLastName(rs.getString("last_name"));
        instructor.setEmail(rs.getString("email"));
        instructor.setPhone(rs.getString("phone"));
        instructor.setSpecialization(rs.getString("specialization"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            instructor.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return instructor;
    }
}
