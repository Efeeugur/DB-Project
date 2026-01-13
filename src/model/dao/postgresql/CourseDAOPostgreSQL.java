package model.dao.postgresql;

import model.dao.CourseDAO;
import model.dao.EnrollmentDAO;
import model.entity.Course;
import model.entity.Student;
import util.DatabaseConnection;
import util.AppLogger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseDAOPostgreSQL implements CourseDAO {
    
    private final DatabaseConnection dbConnection;
    private EnrollmentDAO enrollmentDAO;
    
    public CourseDAOPostgreSQL() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    public void setEnrollmentDAO(EnrollmentDAO enrollmentDAO) {
        this.enrollmentDAO = enrollmentDAO;
    }
    
    @Override
    public Course save(Course course) {
        String sql = "INSERT INTO courses (name, description, term, skill_level, instructor_id, " +
                     "max_capacity, fee, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, course.getName());
            stmt.setString(2, course.getDescription());
            stmt.setString(3, course.getTerm().toString());
            stmt.setString(4, course.getSkillLevel().toString());
            stmt.setInt(5, course.getInstructorId());
            stmt.setInt(6, course.getMaxCapacity());
            stmt.setBigDecimal(7, course.getFee());
            stmt.setDate(8, Date.valueOf(course.getStartDate()));
            stmt.setDate(9, Date.valueOf(course.getEndDate()));
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                course.setId(rs.getInt("id"));
            }
            
            AppLogger.info("Course saved: " + course.getName());
            return course;
            
        } catch (SQLException e) {
            AppLogger.error("Error saving course: " + e.getMessage());
            throw new RuntimeException("Failed to save course", e);
        }
    }
    
    @Override
    public Course update(Course course) {
        String sql = "UPDATE courses SET name = ?, description = ?, term = ?, skill_level = ?, " +
                     "instructor_id = ?, max_capacity = ?, fee = ?, start_date = ?, end_date = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, course.getName());
            stmt.setString(2, course.getDescription());
            stmt.setString(3, course.getTerm().toString());
            stmt.setString(4, course.getSkillLevel().toString());
            stmt.setInt(5, course.getInstructorId());
            stmt.setInt(6, course.getMaxCapacity());
            stmt.setBigDecimal(7, course.getFee());
            stmt.setDate(8, Date.valueOf(course.getStartDate()));
            stmt.setDate(9, Date.valueOf(course.getEndDate()));
            stmt.setInt(10, course.getId());
            
            stmt.executeUpdate();
            AppLogger.info("Course updated: " + course.getId());
            return course;
            
        } catch (SQLException e) {
            AppLogger.error("Error updating course: " + e.getMessage());
            throw new RuntimeException("Failed to update course", e);
        }
    }
    
    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM courses WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            AppLogger.error("Error deleting course: " + e.getMessage());
            return false;
        }
    }
    
    @Override
    public Optional<Course> findById(int id) {
        String sql = "SELECT * FROM courses WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return Optional.of(mapResultSetToCourse(rs));
            }
            return Optional.empty();
            
        } catch (SQLException e) {
            AppLogger.error("Error finding course by ID: " + e.getMessage());
            return Optional.empty();
        }
    }
    
    @Override
    public List<Course> findAll() {
        String sql = "SELECT * FROM courses ORDER BY id";
        List<Course> courses = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }
            return courses;
            
        } catch (SQLException e) {
            AppLogger.error("Error finding all courses: " + e.getMessage());
            return courses;
        }
    }
    
    @Override
    public int count() {
        String sql = "SELECT COUNT(*) FROM courses";
        
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
    public List<Course> findByTerm(Course.Term term) {
        String sql = "SELECT * FROM courses WHERE term = ? ORDER BY id";
        List<Course> courses = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, term.toString());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }
            return courses;
            
        } catch (SQLException e) {
            return courses;
        }
    }
    
    @Override
    public List<Course> findBySkillLevel(Student.SkillLevel level) {
        String sql = "SELECT * FROM courses WHERE skill_level = ? ORDER BY id";
        List<Course> courses = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, level.toString());
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }
            return courses;
            
        } catch (SQLException e) {
            return courses;
        }
    }
    
    @Override
    public List<Course> findAvailableCourses() {
        String sql = "SELECT c.* FROM courses c " +
                     "LEFT JOIN (SELECT course_id, COUNT(*) as enrollment_count FROM enrollments " +
                     "WHERE status = 'ACTIVE' GROUP BY course_id) e ON c.id = e.course_id " +
                     "WHERE COALESCE(e.enrollment_count, 0) < c.max_capacity ORDER BY c.id";
        List<Course> courses = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }
            return courses;
            
        } catch (SQLException e) {
            return courses;
        }
    }
    
    @Override
    public List<Course> findByInstructorId(int instructorId) {
        String sql = "SELECT * FROM courses WHERE instructor_id = ? ORDER BY id";
        List<Course> courses = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, instructorId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }
            return courses;
            
        } catch (SQLException e) {
            return courses;
        }
    }
    
    @Override
    public List<Course> searchByName(String name) {
        String sql = "SELECT * FROM courses WHERE LOWER(name) LIKE LOWER(?) ORDER BY name";
        List<Course> courses = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                courses.add(mapResultSetToCourse(rs));
            }
            return courses;
            
        } catch (SQLException e) {
            return courses;
        }
    }
    
    private Course mapResultSetToCourse(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setId(rs.getInt("id"));
        course.setName(rs.getString("name"));
        course.setDescription(rs.getString("description"));
        course.setTerm(Course.Term.valueOf(rs.getString("term")));
        course.setSkillLevel(Student.SkillLevel.valueOf(rs.getString("skill_level")));
        course.setInstructorId(rs.getInt("instructor_id"));
        course.setMaxCapacity(rs.getInt("max_capacity"));
        course.setFee(rs.getBigDecimal("fee"));
        
        Date startDate = rs.getDate("start_date");
        if (startDate != null) {
            course.setStartDate(startDate.toLocalDate());
        }
        
        Date endDate = rs.getDate("end_date");
        if (endDate != null) {
            course.setEndDate(endDate.toLocalDate());
        }
        
        return course;
    }
}
