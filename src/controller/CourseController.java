package controller;

import model.dao.CourseDAO;
import model.dao.EnrollmentDAO;
import model.dao.InstructorDAO;
import model.dao.SessionDAO;
import model.entity.Course;
import model.entity.Enrollment;
import model.entity.Session;
import model.entity.Student;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Controller for Course operations.
 */
public class CourseController {
    
    private final CourseDAO courseDAO;
    private final InstructorDAO instructorDAO;
    private final SessionDAO sessionDAO;
    private EnrollmentDAO enrollmentDAO;
    
    public CourseController(CourseDAO courseDAO, InstructorDAO instructorDAO, SessionDAO sessionDAO) {
        this.courseDAO = courseDAO;
        this.instructorDAO = instructorDAO;
        this.sessionDAO = sessionDAO;
    }
    
    public void setEnrollmentDAO(EnrollmentDAO enrollmentDAO) {
        this.enrollmentDAO = enrollmentDAO;
    }
    
    /**
     * Creates a new course.
     */
    public Course createCourse(String name, String description, Course.Term term,
                               Student.SkillLevel skillLevel, int instructorId,
                               int maxCapacity, BigDecimal fee,
                               LocalDate startDate, LocalDate endDate) {
        // Validate instructor exists
        if (instructorDAO.findById(instructorId).isEmpty()) {
            throw new IllegalArgumentException("Instructor not found.");
        }
        
        Course course = new Course();
        course.setName(name);
        course.setDescription(description);
        course.setTerm(term);
        course.setSkillLevel(skillLevel);
        course.setInstructorId(instructorId);
        course.setMaxCapacity(maxCapacity);
        course.setFee(fee);
        course.setStartDate(startDate);
        course.setEndDate(endDate);
        
        return courseDAO.save(course);
    }
    
    /**
     * Gets a course by ID.
     */
    public Optional<Course> getCourseById(int id) {
        return courseDAO.findById(id);
    }
    
    /**
     * Gets all courses.
     */
    public List<Course> getAllCourses() {
        return courseDAO.findAll();
    }
    
    /**
     * Updates course information.
     * Validates that new capacity is not less than current active enrollments.
     */
    public Course updateCourse(Course course) {
        // Check if capacity is being reduced below active enrollment count
        if (enrollmentDAO != null) {
            List<Enrollment> enrollments = enrollmentDAO.findByCourseId(course.getId());
            int activeCount = (int) enrollments.stream()
                .filter(e -> e.getStatus() == Enrollment.Status.ACTIVE)
                .count();
            if (course.getMaxCapacity() < activeCount) {
                throw new IllegalArgumentException(
                    "Cannot reduce capacity to " + course.getMaxCapacity() + 
                    ". There are currently " + activeCount + " active students enrolled.");
            }
        }
        return courseDAO.update(course);
    }
    
    /**
     * Deletes a course.
     */
    public boolean deleteCourse(int id) {
        return courseDAO.delete(id);
    }
    
    /**
     * Gets courses by term.
     */
    public List<Course> getCoursesByTerm(Course.Term term) {
        return courseDAO.findByTerm(term);
    }
    
    /**
     * Gets courses by skill level.
     */
    public List<Course> getCoursesBySkillLevel(Student.SkillLevel level) {
        return courseDAO.findBySkillLevel(level);
    }
    
    /**
     * Gets available courses (not full).
     */
    public List<Course> getAvailableCourses() {
        return courseDAO.findAvailableCourses();
    }
    
    /**
     * Creates a session for a course.
     */
    public Session createSession(int courseId, LocalDate sessionDate, 
                                 LocalTime startTime, LocalTime endTime, String topic) {
        if (courseDAO.findById(courseId).isEmpty()) {
            throw new IllegalArgumentException("Course not found.");
        }
        
        Session session = new Session();
        session.setCourseId(courseId);
        session.setSessionDate(sessionDate);
        session.setStartTime(startTime);
        session.setEndTime(endTime);
        session.setTopic(topic);
        
        return sessionDAO.save(session);
    }
    
    /**
     * Convenience method to add session with string parameters.
     */
    public Session addSession(int courseId, String sessionDate, 
                             String startTime, String endTime, String topic) {
        if (sessionDate.isEmpty() || startTime.isEmpty() || endTime.isEmpty() || topic.isEmpty()) {
            throw new IllegalArgumentException("All session fields are required.");
        }
        
        try {
            LocalDate date = LocalDate.parse(sessionDate);
            LocalTime start = LocalTime.parse(startTime);
            LocalTime end = LocalTime.parse(endTime);
            return createSession(courseId, date, start, end, topic);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date/time format. Use YYYY-MM-DD for date and HH:MM for time.");
        }
    }
    
    /**
     * Gets all sessions for a course.
     */
    public List<Session> getSessionsByCourse(int courseId) {
        return sessionDAO.findByCourseId(courseId);
    }
    
    /**
     * Gets a session by ID.
     */
    public Optional<Session> getSession(int sessionId) {
        return sessionDAO.findById(sessionId);
    }
    
    /**
     * Updates a session.
     */
    public Session updateSession(int sessionId, String sessionDate, 
                                String startTime, String endTime, String topic) {
        Optional<Session> sessionOpt = sessionDAO.findById(sessionId);
        if (sessionOpt.isEmpty()) {
            throw new IllegalArgumentException("Session not found.");
        }
        
        Session session = sessionOpt.get();
        session.setSessionDate(LocalDate.parse(sessionDate));
        session.setStartTime(LocalTime.parse(startTime));
        session.setEndTime(LocalTime.parse(endTime));
        session.setTopic(topic);
        
        return sessionDAO.update(session);
    }
    
    /**
     * Deletes a session.
     */
    public boolean deleteSession(int sessionId) {
        return sessionDAO.delete(sessionId);
    }
    
    /**
     * Gets total course count.
     */
    public int getCourseCount() {
        return courseDAO.count();
    }
}
