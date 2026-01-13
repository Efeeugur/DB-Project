package controller;

import model.dao.InstructorDAO;
import model.dao.impl.InstructorDAOImpl;
import model.entity.Instructor;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for InstructorController.
 */
class InstructorControllerTest {
    
    private InstructorController controller;
    private InstructorDAO instructorDAO;
    
    @BeforeEach
    void setUp() {
        instructorDAO = new InstructorDAOImpl();
        controller = new InstructorController(instructorDAO);
    }
    
    @Test
    @DisplayName("Should register a new instructor successfully")
    void testRegisterInstructor() {
        Instructor instructor = controller.registerInstructor(
            "Jane", "Smith", "jane.smith@email.com", "5551234567", "Painting"
        );
        
        assertNotNull(instructor);
        assertTrue(instructor.getId() > 0);
        assertEquals("Jane", instructor.getFirstName());
        assertEquals("Smith", instructor.getLastName());
        assertEquals("jane.smith@email.com", instructor.getEmail());
        assertEquals("Painting", instructor.getSpecialization());
    }
    
    @Test
    @DisplayName("Should throw exception for duplicate email")
    void testRegisterDuplicateEmail() {
        controller.registerInstructor("John", "Doe", "test@email.com", "123", "Art");
        
        assertThrows(IllegalArgumentException.class, () -> {
            controller.registerInstructor("Jane", "Smith", "test@email.com", "456", "Music");
        });
    }
    
    @Test
    @DisplayName("Should get instructor by ID")
    void testGetInstructorById() {
        Instructor created = controller.registerInstructor(
            "Alice", "Wonder", "alice@email.com", "111", "Sculpture"
        );
        
        var result = controller.getInstructorById(created.getId());
        
        assertTrue(result.isPresent());
        assertEquals("Alice", result.get().getFirstName());
        assertEquals("Sculpture", result.get().getSpecialization());
    }
    
    @Test
    @DisplayName("Should return empty for non-existent instructor")
    void testGetNonExistentInstructor() {
        var result = controller.getInstructorById(9999);
        assertTrue(result.isEmpty());
    }
    
    @Test
    @DisplayName("Should get all instructors")
    void testGetAllInstructors() {
        controller.registerInstructor("User1", "Test", "user1@email.com", "1", "Drawing");
        controller.registerInstructor("User2", "Test", "user2@email.com", "2", "Painting");
        
        var instructors = controller.getAllInstructors();
        
        assertEquals(2, instructors.size());
    }
    
    @Test
    @DisplayName("Should update instructor")
    void testUpdateInstructor() {
        Instructor instructor = controller.registerInstructor(
            "Bob", "Builder", "bob@email.com", "555", "Architecture"
        );
        
        instructor.setFirstName("Robert");
        instructor.setSpecialization("Urban Design");
        Instructor updated = controller.updateInstructor(instructor);
        
        assertEquals("Robert", updated.getFirstName());
        assertEquals("Urban Design", updated.getSpecialization());
    }
    
    @Test
    @DisplayName("Should delete instructor")
    void testDeleteInstructor() {
        Instructor instructor = controller.registerInstructor(
            "Delete", "Me", "delete@email.com", "999", "Photography"
        );
        
        boolean deleted = controller.deleteInstructor(instructor.getId());
        
        assertTrue(deleted);
        assertTrue(controller.getInstructorById(instructor.getId()).isEmpty());
    }
    
    @Test
    @DisplayName("Should get instructors by specialization")
    void testGetInstructorsBySpecialization() {
        controller.registerInstructor("Painter1", "Test", "p1@email.com", "1", "Painting");
        controller.registerInstructor("Painter2", "Test", "p2@email.com", "2", "Painting");
        controller.registerInstructor("Sculptor", "Test", "s1@email.com", "3", "Sculpture");
        
        var painters = controller.getInstructorsBySpecialization("Painting");
        
        assertEquals(2, painters.size());
        assertTrue(painters.stream().allMatch(i -> "Painting".equals(i.getSpecialization())));
    }
    
    @Test
    @DisplayName("Should search instructors by name")
    void testSearchInstructors() {
        controller.registerInstructor("TestSearch", "User", "search@email.com", "1", "Art");
        controller.registerInstructor("Another", "Person", "another@email.com", "2", "Music");
        
        var results = controller.searchInstructors("TestSearch");
        
        assertEquals(1, results.size());
        assertEquals("TestSearch", results.get(0).getFirstName());
    }
    
    @Test
    @DisplayName("Should search instructors by partial name (case-insensitive)")
    void testSearchInstructorsPartial() {
        controller.registerInstructor("John", "Smith", "john@email.com", "1", "Art");
        controller.registerInstructor("Johnny", "Doe", "johnny@email.com", "2", "Music");
        
        var results = controller.searchInstructors("john");
        
        assertTrue(results.size() >= 1);
    }
    
    @Test
    @DisplayName("Should get correct instructor count")
    void testGetInstructorCount() {
        assertEquals(0, controller.getInstructorCount());
        
        controller.registerInstructor("Count1", "Test", "count1@email.com", "1", "Art");
        controller.registerInstructor("Count2", "Test", "count2@email.com", "2", "Music");
        
        assertEquals(2, controller.getInstructorCount());
    }
    
    @Test
    @DisplayName("Should return false when deleting non-existent instructor")
    void testDeleteNonExistent() {
        boolean result = controller.deleteInstructor(9999);
        assertFalse(result);
    }
    
    @Test
    @DisplayName("Should handle empty specialization search")
    void testGetInstructorsByNonExistentSpecialization() {
        controller.registerInstructor("Test", "User", "test@email.com", "1", "Painting");
        
        var results = controller.getInstructorsBySpecialization("NonExistent");
        
        assertTrue(results.isEmpty());
    }
}
