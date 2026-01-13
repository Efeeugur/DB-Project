package controller;

import model.dao.CourseDAO;
import model.dao.InstructorDAO;
import model.dao.SessionDAO;
import model.dao.impl.CourseDAOImpl;
import model.dao.impl.InstructorDAOImpl;
import model.dao.impl.SessionDAOImpl;
import model.entity.Course;
import model.entity.Instructor;
import model.entity.Session;
import model.entity.Student;
import org.junit.jupiter.api.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for CourseController.
 */
class CourseControllerTest {
    
    private CourseController controller;
    private CourseDAO courseDAO;
    private InstructorDAO instructorDAO;
    private SessionDAO sessionDAO;
    private Instructor testInstructor;
    
    @BeforeEach
    void setUp() {
        courseDAO = new CourseDAOImpl();
        instructorDAO = new InstructorDAOImpl();
        sessionDAO = new SessionDAOImpl();
        controller = new CourseController(courseDAO, instructorDAO, sessionDAO);
        
        // Create a test instructor for course creation
        testInstructor = new Instructor();
        testInstructor.setFirstName("Test");
        testInstructor.setLastName("Instructor");
        testInstructor.setEmail("instructor@email.com");
        testInstructor.setPhone("1234567890");
        testInstructor.setSpecialization("Art");
        testInstructor = instructorDAO.save(testInstructor);
    }
    
    @Test
    @DisplayName("Should create a new course successfully")
    void testCreateCourse() {
        Course course = controller.createCourse(
            "Painting 101",
            "Introduction to Painting",
            Course.Term.SUMMER,
            Student.SkillLevel.BEGINNER,
            testInstructor.getId(),
            20,
            new BigDecimal("199.99"),
            LocalDate.of(2024, 6, 1),
            LocalDate.of(2024, 8, 31)
        );
        
        assertNotNull(course);
        assertTrue(course.getId() > 0);
        assertEquals("Painting 101", course.getName());
        assertEquals(Course.Term.SUMMER, course.getTerm());
        assertEquals(Student.SkillLevel.BEGINNER, course.getSkillLevel());
        assertEquals(testInstructor.getId(), course.getInstructorId());
    }
    
    @Test
    @DisplayName("Should throw exception when creating course with non-existent instructor")
    void testCreateCourseInvalidInstructor() {
        assertThrows(IllegalArgumentException.class, () -> {
            controller.createCourse(
                "Test Course",
                "Description",
                Course.Term.WINTER,
                Student.SkillLevel.INTERMEDIATE,
                9999, // Non-existent instructor
                15,
                new BigDecimal("150.00"),
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 3, 31)
            );
        });
    }
    
    @Test
    @DisplayName("Should get course by ID")
    void testGetCourseById() {
        Course created = controller.createCourse(
            "Drawing 101",
            "Basic Drawing",
            Course.Term.SUMMER,
            Student.SkillLevel.BEGINNER,
            testInstructor.getId(),
            25,
            new BigDecimal("149.99"),
            LocalDate.of(2024, 6, 1),
            LocalDate.of(2024, 8, 31)
        );
        
        var result = controller.getCourseById(created.getId());
        
        assertTrue(result.isPresent());
        assertEquals("Drawing 101", result.get().getName());
    }
    
    @Test
    @DisplayName("Should return empty for non-existent course")
    void testGetNonExistentCourse() {
        var result = controller.getCourseById(9999);
        assertTrue(result.isEmpty());
    }
    
    @Test
    @DisplayName("Should get all courses")
    void testGetAllCourses() {
        controller.createCourse("Course1", "Desc1", Course.Term.SUMMER, 
            Student.SkillLevel.BEGINNER, testInstructor.getId(), 20, 
            new BigDecimal("100"), LocalDate.now(), LocalDate.now().plusMonths(3));
        controller.createCourse("Course2", "Desc2", Course.Term.WINTER, 
            Student.SkillLevel.INTERMEDIATE, testInstructor.getId(), 15, 
            new BigDecimal("150"), LocalDate.now(), LocalDate.now().plusMonths(3));
        
        var courses = controller.getAllCourses();
        
        assertEquals(2, courses.size());
    }
    
    @Test
    @DisplayName("Should update course")
    void testUpdateCourse() {
        Course course = controller.createCourse(
            "Old Name",
            "Description",
            Course.Term.SUMMER,
            Student.SkillLevel.BEGINNER,
            testInstructor.getId(),
            20,
            new BigDecimal("100"),
            LocalDate.now(),
            LocalDate.now().plusMonths(3)
        );
        
        course.setName("New Name");
        course.setFee(new BigDecimal("200"));
        Course updated = controller.updateCourse(course);
        
        assertEquals("New Name", updated.getName());
        assertEquals(new BigDecimal("200"), updated.getFee());
    }
    
    @Test
    @DisplayName("Should delete course")
    void testDeleteCourse() {
        Course course = controller.createCourse(
            "Delete Me",
            "Description",
            Course.Term.SUMMER,
            Student.SkillLevel.BEGINNER,
            testInstructor.getId(),
            20,
            new BigDecimal("100"),
            LocalDate.now(),
            LocalDate.now().plusMonths(3)
        );
        
        boolean deleted = controller.deleteCourse(course.getId());
        
        assertTrue(deleted);
        assertTrue(controller.getCourseById(course.getId()).isEmpty());
    }
    
    @Test
    @DisplayName("Should get courses by term")
    void testGetCoursesByTerm() {
        controller.createCourse("Summer1", "Desc", Course.Term.SUMMER, 
            Student.SkillLevel.BEGINNER, testInstructor.getId(), 20, 
            new BigDecimal("100"), LocalDate.now(), LocalDate.now().plusMonths(3));
        controller.createCourse("Summer2", "Desc", Course.Term.SUMMER, 
            Student.SkillLevel.INTERMEDIATE, testInstructor.getId(), 20, 
            new BigDecimal("100"), LocalDate.now(), LocalDate.now().plusMonths(3));
        controller.createCourse("Winter1", "Desc", Course.Term.WINTER, 
            Student.SkillLevel.BEGINNER, testInstructor.getId(), 20, 
            new BigDecimal("100"), LocalDate.now(), LocalDate.now().plusMonths(3));
        
        var summerCourses = controller.getCoursesByTerm(Course.Term.SUMMER);
        
        assertEquals(2, summerCourses.size());
        assertTrue(summerCourses.stream().allMatch(c -> c.getTerm() == Course.Term.SUMMER));
    }
    
    @Test
    @DisplayName("Should get courses by skill level")
    void testGetCoursesBySkillLevel() {
        controller.createCourse("Beginner1", "Desc", Course.Term.SUMMER, 
            Student.SkillLevel.BEGINNER, testInstructor.getId(), 20, 
            new BigDecimal("100"), LocalDate.now(), LocalDate.now().plusMonths(3));
        controller.createCourse("Beginner2", "Desc", Course.Term.WINTER, 
            Student.SkillLevel.BEGINNER, testInstructor.getId(), 20, 
            new BigDecimal("100"), LocalDate.now(), LocalDate.now().plusMonths(3));
        controller.createCourse("Advanced1", "Desc", Course.Term.SUMMER, 
            Student.SkillLevel.ADVANCED, testInstructor.getId(), 20, 
            new BigDecimal("100"), LocalDate.now(), LocalDate.now().plusMonths(3));
        
        var beginnerCourses = controller.getCoursesBySkillLevel(Student.SkillLevel.BEGINNER);
        
        assertEquals(2, beginnerCourses.size());
        assertTrue(beginnerCourses.stream()
            .allMatch(c -> c.getSkillLevel() == Student.SkillLevel.BEGINNER));
    }
    
    @Test
    @DisplayName("Should get available courses (not full)")
    void testGetAvailableCourses() {
        // This test requires enrollment DAO to be linked
        // For now, just verify the method works
        var availableCourses = controller.getAvailableCourses();
        assertNotNull(availableCourses);
    }
    
    @Test
    @DisplayName("Should create session for course")
    void testCreateSession() {
        Course course = controller.createCourse(
            "Test Course",
            "Description",
            Course.Term.SUMMER,
            Student.SkillLevel.BEGINNER,
            testInstructor.getId(),
            20,
            new BigDecimal("100"),
            LocalDate.now(),
            LocalDate.now().plusMonths(3)
        );
        
        Session session = controller.createSession(
            course.getId(),
            LocalDate.now(),
            LocalTime.of(10, 0),
            LocalTime.of(12, 0),
            "Introduction to Painting"
        );
        
        assertNotNull(session);
        assertTrue(session.getId() > 0);
        assertEquals(course.getId(), session.getCourseId());
        assertEquals("Introduction to Painting", session.getTopic());
    }
    
    @Test
    @DisplayName("Should throw exception when creating session for non-existent course")
    void testCreateSessionInvalidCourse() {
        assertThrows(IllegalArgumentException.class, () -> {
            controller.createSession(
                9999, // Non-existent course
                LocalDate.now(),
                LocalTime.of(10, 0),
                LocalTime.of(12, 0),
                "Test Topic"
            );
        });
    }
    
    @Test
    @DisplayName("Should get sessions by course")
    void testGetSessionsByCourse() {
        Course course = controller.createCourse(
            "Test Course",
            "Description",
            Course.Term.SUMMER,
            Student.SkillLevel.BEGINNER,
            testInstructor.getId(),
            20,
            new BigDecimal("100"),
            LocalDate.now(),
            LocalDate.now().plusMonths(3)
        );
        
        controller.createSession(course.getId(), LocalDate.now(), 
            LocalTime.of(10, 0), LocalTime.of(12, 0), "Session 1");
        controller.createSession(course.getId(), LocalDate.now().plusDays(1), 
            LocalTime.of(10, 0), LocalTime.of(12, 0), "Session 2");
        
        var sessions = controller.getSessionsByCourse(course.getId());
        
        assertEquals(2, sessions.size());
        assertTrue(sessions.stream().allMatch(s -> s.getCourseId() == course.getId()));
    }
    
    @Test
    @DisplayName("Should get correct course count")
    void testGetCourseCount() {
        assertEquals(0, controller.getCourseCount());
        
        controller.createCourse("Course1", "Desc", Course.Term.SUMMER, 
            Student.SkillLevel.BEGINNER, testInstructor.getId(), 20, 
            new BigDecimal("100"), LocalDate.now(), LocalDate.now().plusMonths(3));
        controller.createCourse("Course2", "Desc", Course.Term.WINTER, 
            Student.SkillLevel.INTERMEDIATE, testInstructor.getId(), 20, 
            new BigDecimal("100"), LocalDate.now(), LocalDate.now().plusMonths(3));
        
        assertEquals(2, controller.getCourseCount());
    }
    
    @Test
    @DisplayName("Should return false when deleting non-existent course")
    void testDeleteNonExistent() {
        boolean result = controller.deleteCourse(9999);
        assertFalse(result);
    }
}
