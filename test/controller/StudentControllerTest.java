package controller;

import model.dao.StudentDAO;
import model.dao.SkillTestDAO;
import model.dao.impl.StudentDAOImpl;
import model.dao.impl.SkillTestDAOImpl;
import model.entity.Student;
import model.entity.SkillTest;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for StudentController.
 */
class StudentControllerTest {
    
    private StudentController controller;
    private StudentDAO studentDAO;
    private SkillTestDAO skillTestDAO;
    
    @BeforeEach
    void setUp() {
        studentDAO = new StudentDAOImpl();
        skillTestDAO = new SkillTestDAOImpl();
        controller = new StudentController(studentDAO, skillTestDAO);
    }
    
    @Test
    @DisplayName("Should register a new student successfully")
    void testRegisterStudent() {
        Student student = controller.registerStudent(
            "John", "Doe", "john.doe@email.com", "1234567890", "1990-01-15"
        );
        
        assertNotNull(student);
        assertTrue(student.getId() > 0);
        assertEquals("John", student.getFirstName());
        assertEquals("Doe", student.getLastName());
        assertEquals("john.doe@email.com", student.getEmail());
        assertEquals(Student.SkillLevel.BEGINNER, student.getSkillLevel());
    }
    
    @Test
    @DisplayName("Should throw exception for duplicate email")
    void testRegisterDuplicateEmail() {
        controller.registerStudent("John", "Doe", "test@email.com", "123", "1990-01-01");
        
        assertThrows(IllegalArgumentException.class, () -> {
            controller.registerStudent("Jane", "Smith", "test@email.com", "456", "1992-02-02");
        });
    }
    
    @Test
    @DisplayName("Should get student by ID")
    void testGetStudentById() {
        Student created = controller.registerStudent(
            "Alice", "Wonder", "alice@email.com", "111", "1995-05-05"
        );
        
        var result = controller.getStudentById(created.getId());
        
        assertTrue(result.isPresent());
        assertEquals("Alice", result.get().getFirstName());
    }
    
    @Test
    @DisplayName("Should return empty for non-existent student")
    void testGetNonExistentStudent() {
        var result = controller.getStudentById(9999);
        assertTrue(result.isEmpty());
    }
    
    @Test
    @DisplayName("Should get all students")
    void testGetAllStudents() {
        controller.registerStudent("User1", "Test", "user1@email.com", "1", "2000-01-01");
        controller.registerStudent("User2", "Test", "user2@email.com", "2", "2000-02-02");
        
        var students = controller.getAllStudents();
        
        assertEquals(2, students.size());
    }
    
    @Test
    @DisplayName("Should update student")
    void testUpdateStudent() {
        Student student = controller.registerStudent(
            "Bob", "Builder", "bob@email.com", "555", "1988-08-08"
        );
        
        student.setFirstName("Robert");
        Student updated = controller.updateStudent(student);
        
        assertEquals("Robert", updated.getFirstName());
    }
    
    @Test
    @DisplayName("Should delete student")
    void testDeleteStudent() {
        Student student = controller.registerStudent(
            "Delete", "Me", "delete@email.com", "999", "2001-01-01"
        );
        
        boolean deleted = controller.deleteStudent(student.getId());
        
        assertTrue(deleted);
        assertTrue(controller.getStudentById(student.getId()).isEmpty());
    }
    
    @Test
    @DisplayName("Should conduct skill test and update level - Beginner")
    void testConductSkillTestBeginner() {
        Student student = controller.registerStudent(
            "Test", "Student", "skill@email.com", "111", "2000-01-01"
        );
        
        SkillTest result = controller.conductSkillTest(student.getId(), 30, "Low score");
        
        assertNotNull(result);
        assertEquals(Student.SkillLevel.BEGINNER, result.getAssignedLevel());
        
        var updatedStudent = controller.getStudentById(student.getId());
        assertEquals(Student.SkillLevel.BEGINNER, updatedStudent.get().getSkillLevel());
    }
    
    @Test
    @DisplayName("Should conduct skill test and update level - Intermediate")
    void testConductSkillTestIntermediate() {
        Student student = controller.registerStudent(
            "Test", "Student2", "skill2@email.com", "222", "2000-02-02"
        );
        
        SkillTest result = controller.conductSkillTest(student.getId(), 60, "Medium score");
        
        assertEquals(Student.SkillLevel.INTERMEDIATE, result.getAssignedLevel());
    }
    
    @Test
    @DisplayName("Should conduct skill test and update level - Advanced")
    void testConductSkillTestAdvanced() {
        Student student = controller.registerStudent(
            "Test", "Student3", "skill3@email.com", "333", "2000-03-03"
        );
        
        SkillTest result = controller.conductSkillTest(student.getId(), 90, "High score");
        
        assertEquals(Student.SkillLevel.ADVANCED, result.getAssignedLevel());
    }
    
    @Test
    @DisplayName("Should throw exception for skill test on non-existent student")
    void testConductSkillTestNonExistent() {
        assertThrows(IllegalArgumentException.class, () -> {
            controller.conductSkillTest(9999, 50, "Test");
        });
    }
    
    @Test
    @DisplayName("Should get students by skill level")
    void testGetStudentsByLevel() {
        controller.registerStudent("Beg1", "Test", "beg1@email.com", "1", "2000-01-01");
        controller.registerStudent("Beg2", "Test", "beg2@email.com", "2", "2000-02-02");
        
        var beginners = controller.getStudentsByLevel(Student.SkillLevel.BEGINNER);
        
        assertEquals(2, beginners.size());
    }
    
    @Test
    @DisplayName("Should search students by name")
    void testSearchStudents() {
        controller.registerStudent("TestSearch", "User", "search@email.com", "1", "2000-01-01");
        controller.registerStudent("Another", "Person", "another@email.com", "2", "2000-02-02");
        
        var results = controller.searchStudents("TestSearch");
        
        assertEquals(1, results.size());
        assertEquals("TestSearch", results.get(0).getFirstName());
    }
    
    @Test
    @DisplayName("Should get correct student count")
    void testGetStudentCount() {
        assertEquals(0, controller.getStudentCount());
        
        controller.registerStudent("Count1", "Test", "count1@email.com", "1", "2000-01-01");
        controller.registerStudent("Count2", "Test", "count2@email.com", "2", "2000-02-02");
        
        assertEquals(2, controller.getStudentCount());
    }
}
