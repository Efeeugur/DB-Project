package model.dao.postgresql;

import model.dao.SessionDAO;
import model.entity.Session;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SessionDAOPostgreSQL implements SessionDAO {
    
    private final DatabaseConnection dbConnection;
    
    public SessionDAOPostgreSQL() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    @Override
    public Session save(Session session) {
        String sql = "INSERT INTO sessions (course_id, session_date, start_time, end_time, topic) " +
                     "VALUES (?, ?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, session.getCourseId());
            stmt.setDate(2, Date.valueOf(session.getSessionDate()));
            stmt.setTime(3, Time.valueOf(session.getStartTime()));
            stmt.setTime(4, Time.valueOf(session.getEndTime()));
            stmt.setString(5, session.getTopic());
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                session.setId(rs.getInt("id"));
            }
            return session;
            
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save session", e);
        }
    }
    
    @Override
    public Session update(Session session) {
        String sql = "UPDATE sessions SET course_id = ?, session_date = ?, start_time = ?, " +
                     "end_time = ?, topic = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, session.getCourseId());
            stmt.setDate(2, Date.valueOf(session.getSessionDate()));
            stmt.setTime(3, Time.valueOf(session.getStartTime()));
            stmt.setTime(4, Time.valueOf(session.getEndTime()));
            stmt.setString(5, session.getTopic());
            stmt.setInt(6, session.getId());
            
            stmt.executeUpdate();
            return session;
            
        } catch (SQLException e) {
           throw new RuntimeException("Failed to update session", e);
        }
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM sessions WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            return false;
        }
    }
    
    @Override
    public Optional<Session> findById(int id) {
        String sql = "SELECT * FROM sessions WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToSession(rs));
            }
            return Optional.empty();
            
        } catch (SQLException e) {
            return Optional.empty();
        }
    }
    
    @Override
    public List<Session> findAll() {
        String sql = "SELECT * FROM sessions ORDER BY id";
        List<Session> sessions = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                sessions.add(mapResultSetToSession(rs));
            }
            return sessions;
            
        } catch (SQLException e) {
            return sessions;
        }
    }
    
    @Override
    public int count() {
        String sql = "SELECT COUNT(*) FROM sessions";
        
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
    public List<Session> findByCourseId(int courseId) {
        String sql = "SELECT * FROM sessions WHERE course_id = ? ORDER BY session_date, start_time";
        List<Session> sessions = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, courseId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                sessions.add(mapResultSetToSession(rs));
            }
            return sessions;
            
        } catch (SQLException e) {
            return sessions;
        }
    }
    
    private Session mapResultSetToSession(ResultSet rs) throws SQLException {
        Session session = new Session();
        session.setId(rs.getInt("id"));
        session.setCourseId(rs.getInt("course_id"));
        
        Date sessionDate = rs.getDate("session_date");
        if (sessionDate != null) {
            session.setSessionDate(sessionDate.toLocalDate());
        }
        
        Time startTime = rs.getTime("start_time");
        if (startTime != null) {
            session.setStartTime(startTime.toLocalTime());
        }
        
        Time endTime = rs.getTime("end_time");
        if (endTime != null) {
            session.setEndTime(endTime.toLocalTime());
        }
        
        session.setTopic(rs.getString("topic"));
        
        return session;
    }
}
