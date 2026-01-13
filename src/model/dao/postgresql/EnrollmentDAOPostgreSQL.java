package model.dao.postgresql;

import model.dao.EnrollmentDAO;
import model.entity.Enrollment;
import util.DatabaseConnection;
import util.AppLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnrollmentDAOPostgreSQL implements EnrollmentDAO {
    
    private final DatabaseConnection dbConnection;
    
    public EnrollmentDAOPostgreSQL() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    @Override
    public Enrollment save(Enrollment enrollment) {
        String sql = "INSERT INTO enrollments (student_id, course_id, status) VALUES (?, ?, ?) RETURNING id";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, enrollment.getStudentId());
            stmt.setInt(2, enrollment.getCourseId());
            stmt.setString(3, enrollment.getStatus().toString());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                enrollment.setId(rs.getInt("id"));
            }
            
            AppLogger.info("Enrollment saved: " + enrollment.getId());
            return enrollment;
            
        } catch (SQLException e) {
            AppLogger.error("Error saving enrollment: " + e.getMessage());
            throw new RuntimeException("Failed to save enrollment", e);
        }
    }
    
    @Override
    public Enrollment update(Enrollment enrollment) {
        String sql = "UPDATE enrollments SET student_id = ?, course_id = ?, status = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, enrollment.getStudentId());
            stmt.setInt(2, enrollment.getCourseId());
            stmt.setString(3, enrollment.getStatus().toString());
            stmt.setInt(4, enrollment.getId());
            
            stmt.executeUpdate();
            return enrollment;
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update enrollment", e);
        }
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM enrollments WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            return false;
        }
    }
    
    @Override
    public Optional<Enrollment> findById(int id) {
        String sql = "SELECT * FROM enrollments WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToEnrollment(rs));
            }
            return Optional.empty();
            
        } catch (SQLException e) {
            return Optional.empty();
        }
    }
    
    @Override
    public List<Enrollment> findAll() {
        String sql = "SELECT * FROM enrollments ORDER BY id";
        List<Enrollment> enrollments = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                enrollments.add(mapResultSetToEnrollment(rs));
            }
            return enrollments;
            
        } catch (SQLException e) {
            return enrollments;
        }
    }
    
    @Override
    public int count() {
        String sql = "SELECT COUNT(*) FROM enrollments";
        
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
    public List<Enrollment> findByStudentId(int studentId) {
        String sql = "SELECT * FROM enrollments WHERE student_id = ? ORDER BY id";
        List<Enrollment> enrollments = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                enrollments.add(mapResultSetToEnrollment(rs));
            }
            return enrollments;
            
        } catch (SQLException e) {
            return enrollments;
        }
    }
    
    @Override
    public List<Enrollment> findByCourseId(int courseId) {
        String sql = "SELECT * FROM enrollments WHERE course_id = ? ORDER BY id";
        List<Enrollment> enrollments = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, courseId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                enrollments.add(mapResultSetToEnrollment(rs));
            }
            return enrollments;
            
        } catch (SQLException e) {
            return enrollments;
        }
    }
    
    @Override
    public Enrollment findByStudentAndCourse(int studentId, int courseId) {
        String sql = "SELECT * FROM enrollments WHERE student_id = ? AND course_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToEnrollment(rs);
            }
            return null;
            
        } catch (SQLException e) {
            return null;
        }
    }
    
    @Override
    public int countByCourseId(int courseId) {
        String sql = "SELECT COUNT(*) FROM enrollments WHERE course_id = ? AND status = 'ACTIVE'";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, courseId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
            
        } catch (SQLException e) {
            return 0;
        }
    }
    
    @Override
    public List<Enrollment> findActiveEnrollments() {
        String sql = "SELECT * FROM enrollments WHERE status = 'ACTIVE' ORDER BY id";
        List<Enrollment> enrollments = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                enrollments.add(mapResultSetToEnrollment(rs));
            }
            return enrollments;
            
        } catch (SQLException e) {
            return enrollments;
        }
    }
    
    private Enrollment mapResultSetToEnrollment(ResultSet rs) throws SQLException {
        Enrollment enrollment = new Enrollment();
        enrollment.setId(rs.getInt("id"));
        enrollment.setStudentId(rs.getInt("student_id"));
        enrollment.setCourseId(rs.getInt("course_id"));
        enrollment.setStatus(Enrollment.Status.valueOf(rs.getString("status")));
        
        Timestamp enrollmentDate = rs.getTimestamp("enrollment_date");
        if (enrollmentDate != null) {
            enrollment.setEnrollmentDate(enrollmentDate.toLocalDateTime());
        }
        
        return enrollment;
    }
}
