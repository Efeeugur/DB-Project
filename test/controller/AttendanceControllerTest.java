package controller;

import model.dao.*;
import model.dao.impl.*;
import model.entity.*;
import org.junit.jupiter.api.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for AttendanceController.
 */
class AttendanceControllerTest {
    
    private AttendanceController controller;
    private AttendanceDAO attendanceDAO;
    private EnrollmentDAO enrollmentDAO;
    private SessionDAO sessionDAO;
    
    private StudentDAO studentDAO;
    private CourseDAO courseDAO;
    private InstructorDAO instructorDAO;
    
    private Enrollment testEnrollment;
    private Session testSession;
    private Student testStudent;
    private Course testCourse;
    
    @BeforeEach
    void setUp() {
        attendanceDAO = new AttendanceDAOImpl();
        enrollmentDAO = new EnrollmentDAOImpl();
        sessionDAO = new SessionDAOImpl();
        studentDAO = new StudentDAOImpl();
        courseDAO = new CourseDAOImpl();
        instructorDAO = new InstructorDAOImpl();
        
        controller = new AttendanceController(attendanceDAO, enrollmentDAO, sessionDAO);
        
        // Create test instructor
        Instructor instructor = new Instructor();
        instructor.setFirstName("Test");
        instructor.setLastName("Instructor");
        instructor.setEmail("instructor@email.com");
        instructor.setPhone("1234567890");
        instructor.setSpecialization("Art");
        instructor = instructorDAO.save(instructor);
        
        // Create test student
        testStudent = new Student();
        testStudent.setFirstName("John");
        testStudent.setLastName("Doe");
        testStudent.setEmail("john@email.com");
        testStudent.setPhone("1234567890");
        testStudent.setDateOfBirth("1995-01-01");
        testStudent.setSkillLevel(Student.SkillLevel.BEGINNER);
        testStudent = studentDAO.save(testStudent);
        
        // Create test course
        testCourse = new Course();
        testCourse.setName("Painting 101");
        testCourse.setDescription("Intro to Painting");
        testCourse.setTerm(Course.Term.SUMMER);
        testCourse.setSkillLevel(Student.SkillLevel.BEGINNER);
        testCourse.setInstructorId(instructor.getId());
        testCourse.setMaxCapacity(20);
        testCourse.setFee(new BigDecimal("199.99"));
        testCourse.setStartDate(LocalDate.now());
        testCourse.setEndDate(LocalDate.now().plusMonths(3));
        testCourse = courseDAO.save(testCourse);
        
        // Create test enrollment
        testEnrollment = new Enrollment();
        testEnrollment.setStudentId(testStudent.getId());
        testEnrollment.setCourseId(testCourse.getId());
        testEnrollment.setStatus(Enrollment.Status.ACTIVE);
        testEnrollment = enrollmentDAO.save(testEnrollment);
        
        // Create test session
        testSession = new Session();
        testSession.setCourseId(testCourse.getId());
        testSession.setSessionDate(LocalDate.now());
        testSession.setStartTime(LocalTime.of(10, 0));
        testSession.setEndTime(LocalTime.of(12, 0));
        testSession.setTopic("Introduction");
        testSession = sessionDAO.save(testSession);
    }
    
    @Test
    @DisplayName("Should record attendance successfully")
    void testRecordAttendance() {
        Attendance attendance = controller.recordAttendance(
            testEnrollment.getId(),
            testSession.getId(),
            Attendance.AttendanceStatus.PRESENT,
            "On time"
        );
        
        assertNotNull(attendance);
        assertTrue(attendance.getId() > 0);
        assertEquals(testEnrollment.getId(), attendance.getEnrollmentId());
        assertEquals(testSession.getId(), attendance.getSessionId());
        assertEquals(Attendance.AttendanceStatus.PRESENT, attendance.getStatus());
        assertEquals("On time", attendance.getNotes());
    }
    
    @Test
    @DisplayName("Should throw exception when recording attendance for non-existent enrollment")
    void testRecordAttendanceInvalidEnrollment() {
        assertThrows(IllegalArgumentException.class, () -> {
            controller.recordAttendance(
                9999,
                testSession.getId(),
                Attendance.AttendanceStatus.PRESENT,
                null
            );
        });
    }
    
    @Test
    @DisplayName("Should throw exception when recording attendance for non-existent session")
    void testRecordAttendanceInvalidSession() {
        assertThrows(IllegalArgumentException.class, () -> {
            controller.recordAttendance(
                testEnrollment.getId(),
                9999,
                Attendance.AttendanceStatus.PRESENT,
                null
            );
        });
    }
    
    @Test
    @DisplayName("Should update existing attendance record")
    void testUpdateExistingAttendance() {
        // First record attendance
        controller.recordAttendance(
            testEnrollment.getId(),
            testSession.getId(),
            Attendance.AttendanceStatus.PRESENT,
            "On time"
        );
        
        // Update the same attendance
        Attendance updated = controller.recordAttendance(
            testEnrollment.getId(),
            testSession.getId(),
            Attendance.AttendanceStatus.LATE,
            "Arrived 15 minutes late"
        );
        
        assertNotNull(updated);
        assertEquals(Attendance.AttendanceStatus.LATE, updated.getStatus());
        assertEquals("Arrived 15 minutes late", updated.getNotes());
    }
    
    @Test
    @DisplayName("Should record different attendance statuses")
    void testRecordDifferentStatuses() {
        // Create additional sessions
        Session session2 = new Session();
        session2.setCourseId(testCourse.getId());
        session2.setSessionDate(LocalDate.now().plusDays(1));
        session2.setStartTime(LocalTime.of(10, 0));
        session2.setEndTime(LocalTime.of(12, 0));
        session2.setTopic("Session 2");
        session2 = sessionDAO.save(session2);
        
        Session session3 = new Session();
        session3.setCourseId(testCourse.getId());
        session3.setSessionDate(LocalDate.now().plusDays(2));
        session3.setStartTime(LocalTime.of(10, 0));
        session3.setEndTime(LocalTime.of(12, 0));
        session3.setTopic("Session 3");
        session3 = sessionDAO.save(session3);
        
        // Record different statuses
        controller.recordAttendance(testEnrollment.getId(), testSession.getId(), 
            Attendance.AttendanceStatus.PRESENT, null);
        controller.recordAttendance(testEnrollment.getId(), session2.getId(), 
            Attendance.AttendanceStatus.LATE, "Late");
        controller.recordAttendance(testEnrollment.getId(), session3.getId(), 
            Attendance.AttendanceStatus.ABSENT, "Sick");
        
        var attendances = controller.getEnrollmentAttendance(testEnrollment.getId());
        
        assertEquals(3, attendances.size());
    }
    
    @Test
    @DisplayName("Should get session attendance")
    void testGetSessionAttendance() {
        // Create another enrollment
        Student student2 = new Student();
        student2.setFirstName("Jane");
        student2.setLastName("Smith");
        student2.setEmail("jane@email.com");
        student2.setPhone("9876543210");
        student2.setDateOfBirth("1996-01-01");
        student2.setSkillLevel(Student.SkillLevel.BEGINNER);
        student2 = studentDAO.save(student2);
        
        Enrollment enrollment2 = new Enrollment();
        enrollment2.setStudentId(student2.getId());
        enrollment2.setCourseId(testCourse.getId());
        enrollment2.setStatus(Enrollment.Status.ACTIVE);
        enrollment2 = enrollmentDAO.save(enrollment2);
        
        // Record attendance for both students
        controller.recordAttendance(testEnrollment.getId(), testSession.getId(), 
            Attendance.AttendanceStatus.PRESENT, null);
        controller.recordAttendance(enrollment2.getId(), testSession.getId(), 
            Attendance.AttendanceStatus.LATE, null);
        
        var sessionAttendance = controller.getSessionAttendance(testSession.getId());
        
        assertEquals(2, sessionAttendance.size());
        assertTrue(sessionAttendance.stream()
            .allMatch(a -> a.getSessionId() == testSession.getId()));
    }
    
    @Test
    @DisplayName("Should get enrollment attendance")
    void testGetEnrollmentAttendance() {
        // Create multiple sessions
        Session session2 = new Session();
        session2.setCourseId(testCourse.getId());
        session2.setSessionDate(LocalDate.now().plusDays(1));
        session2.setStartTime(LocalTime.of(10, 0));
        session2.setEndTime(LocalTime.of(12, 0));
        session2.setTopic("Session 2");
        session2 = sessionDAO.save(session2);
        
        // Record attendance
        controller.recordAttendance(testEnrollment.getId(), testSession.getId(), 
            Attendance.AttendanceStatus.PRESENT, null);
        controller.recordAttendance(testEnrollment.getId(), session2.getId(), 
            Attendance.AttendanceStatus.PRESENT, null);
        
        var enrollmentAttendance = controller.getEnrollmentAttendance(testEnrollment.getId());
        
        assertEquals(2, enrollmentAttendance.size());
        assertTrue(enrollmentAttendance.stream()
            .allMatch(a -> a.getEnrollmentId() == testEnrollment.getId()));
    }
    
    @Test
    @DisplayName("Should calculate attendance percentage - 100%")
    void testCalculateAttendancePercentage100() {
        // Create multiple sessions
        Session session2 = new Session();
        session2.setCourseId(testCourse.getId());
        session2.setSessionDate(LocalDate.now().plusDays(1));
        session2.setStartTime(LocalTime.of(10, 0));
        session2.setEndTime(LocalTime.of(12, 0));
        session2.setTopic("Session 2");
        session2 = sessionDAO.save(session2);
        
        // Record all present
        controller.recordAttendance(testEnrollment.getId(), testSession.getId(), 
            Attendance.AttendanceStatus.PRESENT, null);
        controller.recordAttendance(testEnrollment.getId(), session2.getId(), 
            Attendance.AttendanceStatus.PRESENT, null);
        
        double percentage = controller.calculateAttendancePercentage(testEnrollment.getId());
        
        assertEquals(100.0, percentage, 0.01);
    }
    
    @Test
    @DisplayName("Should calculate attendance percentage - 50%")
    void testCalculateAttendancePercentage50() {
        // Create multiple sessions
        Session session2 = new Session();
        session2.setCourseId(testCourse.getId());
        session2.setSessionDate(LocalDate.now().plusDays(1));
        session2.setStartTime(LocalTime.of(10, 0));
        session2.setEndTime(LocalTime.of(12, 0));
        session2.setTopic("Session 2");
        session2 = sessionDAO.save(session2);
        
        // Record 1 present, 1 absent
        controller.recordAttendance(testEnrollment.getId(), testSession.getId(), 
            Attendance.AttendanceStatus.PRESENT, null);
        controller.recordAttendance(testEnrollment.getId(), session2.getId(), 
            Attendance.AttendanceStatus.ABSENT, null);
        
        double percentage = controller.calculateAttendancePercentage(testEnrollment.getId());
        
        assertEquals(50.0, percentage, 0.01);
    }
    
    @Test
    @DisplayName("Should count LATE as present in percentage calculation")
    void testCalculateAttendancePercentageWithLate() {
        // LATE should count as present according to the implementation
        Session session2 = new Session();
        session2.setCourseId(testCourse.getId());
        session2.setSessionDate(LocalDate.now().plusDays(1));
        session2.setStartTime(LocalTime.of(10, 0));
        session2.setEndTime(LocalTime.of(12, 0));
        session2.setTopic("Session 2");
        session2 = sessionDAO.save(session2);
        
        controller.recordAttendance(testEnrollment.getId(), testSession.getId(), 
            Attendance.AttendanceStatus.LATE, null);
        controller.recordAttendance(testEnrollment.getId(), session2.getId(), 
            Attendance.AttendanceStatus.PRESENT, null);
        
        double percentage = controller.calculateAttendancePercentage(testEnrollment.getId());
        
        assertEquals(100.0, percentage, 0.01);
    }
    
    @Test
    @DisplayName("Should return 0 percentage for no attendance records")
    void testCalculateAttendancePercentageNoRecords() {
        double percentage = controller.calculateAttendancePercentage(testEnrollment.getId());
        
        assertEquals(0.0, percentage, 0.01);
    }
    
    @Test
    @DisplayName("Should handle attendance with notes")
    void testRecordAttendanceWithNotes() {
        Attendance attendance = controller.recordAttendance(
            testEnrollment.getId(),
            testSession.getId(),
            Attendance.AttendanceStatus.ABSENT,
            "Student was sick - doctor's note provided"
        );
        
        assertEquals("Student was sick - doctor's note provided", attendance.getNotes());
    }
    
    @Test
    @DisplayName("Should handle attendance without notes")
    void testRecordAttendanceWithoutNotes() {
        Attendance attendance = controller.recordAttendance(
            testEnrollment.getId(),
            testSession.getId(),
            Attendance.AttendanceStatus.PRESENT,
            null
        );
        
        assertNull(attendance.getNotes());
    }
}
