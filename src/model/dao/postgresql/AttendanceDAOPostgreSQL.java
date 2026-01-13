package model.dao.postgresql;

import model.dao.AttendanceDAO;
import model.entity.Attendance;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AttendanceDAOPostgreSQL implements AttendanceDAO {
    
    private final DatabaseConnection dbConnection;
    
    public AttendanceDAOPostgreSQL() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    @Override
    public Attendance save(Attendance attendance) {
        String sql = "INSERT INTO attendance (enrollment_id, session_id, status, notes) " +
                     "VALUES (?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, attendance.getEnrollmentId());
            stmt.setInt(2, attendance.getSessionId());
            stmt.setString(3, attendance.getStatus().toString());
            stmt.setString(4, attendance.getNotes());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                attendance.setId(rs.getInt("id"));
            }
            return attendance;
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save attendance", e);
        }
    }
    
    @Override
    public Attendance update(Attendance attendance) {
        String sql = "UPDATE attendance SET enrollment_id = ?, session_id = ?, status = ?, notes = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, attendance.getEnrollmentId());
            stmt.setInt(2, attendance.getSessionId());
            stmt.setString(3, attendance.getStatus().toString());
            stmt.setString(4, attendance.getNotes());
            stmt.setInt(5, attendance.getId());
            
            stmt.executeUpdate();
            return attendance;
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update attendance", e);
        }
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM attendance WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            return false;
        }
    }
    
    @Override
    public Optional<Attendance> findById(int id) {
        String sql = "SELECT * FROM attendance WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToAttendance(rs));
            }
            return Optional.empty();
            
        } catch (SQLException e) {
            return Optional.empty();
        }
    }
    
    @Override
    public List<Attendance> findAll() {
        String sql = "SELECT * FROM attendance ORDER BY id";
        List<Attendance> attendances = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                attendances.add(mapResultSetToAttendance(rs));
            }
            return attendances;
            
        } catch (SQLException e) {
            return attendances;
        }
    }
    
    @Override
    public int count() {
        String sql = "SELECT COUNT(*) FROM attendance";
        
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
    public List<Attendance> findBySessionId(int sessionId) {
        String sql = "SELECT * FROM attendance WHERE session_id = ? ORDER BY id";
        List<Attendance> attendances = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, sessionId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                attendances.add(mapResultSetToAttendance(rs));
            }
            return attendances;
            
        } catch (SQLException e) {
            return attendances;
        }
    }
    
    @Override
    public List<Attendance> findByEnrollmentId(int enrollmentId) {
        String sql = "SELECT * FROM attendance WHERE enrollment_id = ? ORDER BY id";
        List<Attendance> attendances = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, enrollmentId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                attendances.add(mapResultSetToAttendance(rs));
            }
            return attendances;
            
        } catch (SQLException e) {
            return attendances;
        }
    }
    
    @Override
    public Attendance findByEnrollmentAndSession(int enrollmentId, int sessionId) {
        String sql = "SELECT * FROM attendance WHERE enrollment_id = ? AND session_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, enrollmentId);
            stmt.setInt(2, sessionId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToAttendance(rs);
            }
            return null;
            
        } catch (SQLException e) {
            return null;
        }
    }
    
    private Attendance mapResultSetToAttendance(ResultSet rs) throws SQLException {
        Attendance attendance = new Attendance();
        attendance.setId(rs.getInt("id"));
        attendance.setEnrollmentId(rs.getInt("enrollment_id"));
        attendance.setSessionId(rs.getInt("session_id"));
        attendance.setStatus(Attendance.AttendanceStatus.valueOf(rs.getString("status")));
        attendance.setNotes(rs.getString("notes"));
        
        // Note: recorded_at timestamp is not stored in Attendance entity
        // It exists in the database but not used in the Java class
        
        return attendance;
    }
}
