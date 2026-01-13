package controller;

import model.dao.*;
import model.dao.impl.*;
import model.entity.*;
import org.junit.jupiter.api.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for EnrollmentController.
 */
class EnrollmentControllerTest {
    
    private EnrollmentController controller;
    private EnrollmentDAO enrollmentDAO;
    private StudentDAO studentDAO;
    private CourseDAO courseDAO;
    private PaymentDAO paymentDAO;
    private InstructorDAO instructorDAO;
    
    private Student testStudent;
    private Course testCourse;
    private Instructor testInstructor;
    
    @BeforeEach
    void setUp() {
        enrollmentDAO = new EnrollmentDAOImpl();
        studentDAO = new StudentDAOImpl();
        courseDAO = new CourseDAOImpl();
        paymentDAO = new PaymentDAOImpl();
        instructorDAO = new InstructorDAOImpl();
        
        controller = new EnrollmentController(enrollmentDAO, studentDAO, courseDAO, paymentDAO);
        
        // Create test instructor
        testInstructor = new Instructor();
        testInstructor.setFirstName("Test");
        testInstructor.setLastName("Instructor");
        testInstructor.setEmail("instructor@email.com");
        testInstructor.setPhone("1234567890");
        testInstructor.setSpecialization("Art");
        testInstructor = instructorDAO.save(testInstructor);
        
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
        testCourse.setInstructorId(testInstructor.getId());
        testCourse.setMaxCapacity(20);
        testCourse.setFee(new BigDecimal("199.99"));
        testCourse.setStartDate(LocalDate.now());
        testCourse.setEndDate(LocalDate.now().plusMonths(3));
        testCourse = courseDAO.save(testCourse);
    }
    
    @Test
    @DisplayName("Should enroll student successfully")
    void testEnrollStudent() {
        Enrollment enrollment = controller.enrollStudent(testStudent.getId(), testCourse.getId());
        
        assertNotNull(enrollment);
        assertTrue(enrollment.getId() > 0);
        assertEquals(testStudent.getId(), enrollment.getStudentId());
        assertEquals(testCourse.getId(), enrollment.getCourseId());
        assertEquals(Enrollment.Status.ACTIVE, enrollment.getStatus());
        
        // Verify payment was created
        var payments = paymentDAO.findByEnrollmentId(enrollment.getId());
        assertEquals(1, payments.size());
        assertEquals(Payment.PaymentStatus.PENDING, payments.get(0).getStatus());
    }
    
    @Test
    @DisplayName("Should throw exception when enrolling non-existent student")
    void testEnrollNonExistentStudent() {
        assertThrows(IllegalArgumentException.class, () -> {
            controller.enrollStudent(9999, testCourse.getId());
        });
    }
    
    @Test
    @DisplayName("Should throw exception when enrolling in non-existent course")
    void testEnrollInNonExistentCourse() {
        assertThrows(IllegalArgumentException.class, () -> {
            controller.enrollStudent(testStudent.getId(), 9999);
        });
    }
    
    @Test
    @DisplayName("Should throw exception when skill levels don't match")
    void testEnrollSkillLevelMismatch() {
        // Create INTERMEDIATE student
        Student advancedStudent = new Student();
        advancedStudent.setFirstName("Jane");
        advancedStudent.setLastName("Smith");
        advancedStudent.setEmail("jane@email.com");
        advancedStudent.setPhone("9876543210");
        advancedStudent.setDateOfBirth("1996-01-01");
        advancedStudent.setSkillLevel(Student.SkillLevel.INTERMEDIATE);
        advancedStudent = studentDAO.save(advancedStudent);
        
        // testCourse is BEGINNER level
        assertThrows(IllegalArgumentException.class, () -> {
            controller.enrollStudent(advancedStudent.getId(), testCourse.getId());
        });
    }
    
    @Test
    @DisplayName("Should throw exception when student already enrolled")
    void testEnrollAlreadyEnrolled() {
        controller.enrollStudent(testStudent.getId(), testCourse.getId());
        
        assertThrows(IllegalArgumentException.class, () -> {
            controller.enrollStudent(testStudent.getId(), testCourse.getId());
        });
    }
    
    @Test
    @DisplayName("Should throw exception when course is full")
    void testEnrollCourseFull() {
        // Create a course with capacity of 1
        Course smallCourse = new Course();
        smallCourse.setName("Small Course");
        smallCourse.setDescription("Limited Capacity");
        smallCourse.setTerm(Course.Term.SUMMER);
        smallCourse.setSkillLevel(Student.SkillLevel.BEGINNER);
        smallCourse.setInstructorId(testInstructor.getId());
        smallCourse.setMaxCapacity(1);
        smallCourse.setFee(new BigDecimal("100"));
        smallCourse.setStartDate(LocalDate.now());
        smallCourse.setEndDate(LocalDate.now().plusMonths(3));
        smallCourse = courseDAO.save(smallCourse);
        
        // First enrollment should succeed
        controller.enrollStudent(testStudent.getId(), smallCourse.getId());
        
        // Create another student
        Student student2 = new Student();
        student2.setFirstName("Alice");
        student2.setLastName("Wonder");
        student2.setEmail("alice@email.com");
        student2.setPhone("5551234567");
        student2.setDateOfBirth("1997-01-01");
        student2.setSkillLevel(Student.SkillLevel.BEGINNER);
        student2 = studentDAO.save(student2);
        
        // Second enrollment should fail
        assertThrows(IllegalArgumentException.class, () -> {
            controller.enrollStudent(student2.getId(), smallCourse.getId());
        });
    }
    
    @Test
    @DisplayName("Should drop enrollment successfully")
    void testDropEnrollment() {
        Enrollment enrollment = controller.enrollStudent(testStudent.getId(), testCourse.getId());
        
        boolean dropped = controller.dropEnrollment(enrollment.getId());
        
        assertTrue(dropped);
        var updated = enrollmentDAO.findById(enrollment.getId());
        assertTrue(updated.isPresent());
        assertEquals(Enrollment.Status.DROPPED, updated.get().getStatus());
    }
    
    @Test
    @DisplayName("Should return false when dropping non-existent enrollment")
    void testDropNonExistentEnrollment() {
        boolean result = controller.dropEnrollment(9999);
        assertFalse(result);
    }
    
    @Test
    @DisplayName("Should complete enrollment successfully")
    void testCompleteEnrollment() {
        Enrollment enrollment = controller.enrollStudent(testStudent.getId(), testCourse.getId());
        
        boolean completed = controller.completeEnrollment(enrollment.getId());
        
        assertTrue(completed);
        var updated = enrollmentDAO.findById(enrollment.getId());
        assertTrue(updated.isPresent());
        assertEquals(Enrollment.Status.COMPLETED, updated.get().getStatus());
    }
    
    @Test
    @DisplayName("Should return false when completing non-existent enrollment")
    void testCompleteNonExistentEnrollment() {
        boolean result = controller.completeEnrollment(9999);
        assertFalse(result);
    }
    
    @Test
    @DisplayName("Should get student enrollments")
    void testGetStudentEnrollments() {
        // Create another course
        Course course2 = new Course();
        course2.setName("Drawing 101");
        course2.setDescription("Basic Drawing");
        course2.setTerm(Course.Term.WINTER);
        course2.setSkillLevel(Student.SkillLevel.BEGINNER);
        course2.setInstructorId(testInstructor.getId());
        course2.setMaxCapacity(15);
        course2.setFee(new BigDecimal("149.99"));
        course2.setStartDate(LocalDate.now());
        course2.setEndDate(LocalDate.now().plusMonths(3));
        course2 = courseDAO.save(course2);
        
        controller.enrollStudent(testStudent.getId(), testCourse.getId());
        controller.enrollStudent(testStudent.getId(), course2.getId());
        
        var enrollments = controller.getStudentEnrollments(testStudent.getId());
        
        assertEquals(2, enrollments.size());
        assertTrue(enrollments.stream().allMatch(e -> e.getStudentId() == testStudent.getId()));
    }
    
    @Test
    @DisplayName("Should get course enrollments")
    void testGetCourseEnrollments() {
        // Create another student
        Student student2 = new Student();
        student2.setFirstName("Jane");
        student2.setLastName("Smith");
        student2.setEmail("jane@email.com");
        student2.setPhone("9876543210");
        student2.setDateOfBirth("1996-01-01");
        student2.setSkillLevel(Student.SkillLevel.BEGINNER);
        student2 = studentDAO.save(student2);
        
        controller.enrollStudent(testStudent.getId(), testCourse.getId());
        controller.enrollStudent(student2.getId(), testCourse.getId());
        
        var enrollments = controller.getCourseEnrollments(testCourse.getId());
        
        assertEquals(2, enrollments.size());
        assertTrue(enrollments.stream().allMatch(e -> e.getCourseId() == testCourse.getId()));
    }
    
    @Test
    @DisplayName("Should get active enrollments")
    void testGetActiveEnrollments() {
        Enrollment enrollment1 = controller.enrollStudent(testStudent.getId(), testCourse.getId());
        
        // Create another student and enroll
        Student student2 = new Student();
        student2.setFirstName("Jane");
        student2.setLastName("Smith");
        student2.setEmail("jane@email.com");
        student2.setPhone("9876543210");
        student2.setDateOfBirth("1996-01-01");
        student2.setSkillLevel(Student.SkillLevel.BEGINNER);
        student2 = studentDAO.save(student2);
        
        // Create another course
        Course course2 = new Course();
        course2.setName("Drawing 101");
        course2.setDescription("Basic Drawing");
        course2.setTerm(Course.Term.WINTER);
        course2.setSkillLevel(Student.SkillLevel.BEGINNER);
        course2.setInstructorId(testInstructor.getId());
        course2.setMaxCapacity(15);
        course2.setFee(new BigDecimal("149.99"));
        course2.setStartDate(LocalDate.now());
        course2.setEndDate(LocalDate.now().plusMonths(3));
        course2 = courseDAO.save(course2);
        
        Enrollment enrollment2 = controller.enrollStudent(student2.getId(), course2.getId());
        
        // Drop one enrollment
        controller.dropEnrollment(enrollment1.getId());
        
        var activeEnrollments = controller.getActiveEnrollments();
        
        assertEquals(1, activeEnrollments.size());
        assertTrue(activeEnrollments.stream()
            .allMatch(e -> e.getStatus() == Enrollment.Status.ACTIVE));
    }
    
    @Test
    @DisplayName("Should process payment successfully")
    void testProcessPayment() {
        Enrollment enrollment = controller.enrollStudent(testStudent.getId(), testCourse.getId());
        
        Payment payment = controller.processPayment(enrollment.getId(), "Credit Card");
        
        assertNotNull(payment);
        assertEquals(Payment.PaymentStatus.COMPLETED, payment.getStatus());
        assertEquals("Credit Card", payment.getPaymentMethod());
    }
    
    @Test
    @DisplayName("Should throw exception when processing payment for non-existent enrollment")
    void testProcessPaymentNonExistent() {
        assertThrows(IllegalArgumentException.class, () -> {
            controller.processPayment(9999, "Cash");
        });
    }
    
    @Test
    @DisplayName("Should get pending payments")
    void testGetPendingPayments() {
        controller.enrollStudent(testStudent.getId(), testCourse.getId());
        
        // Create another student and enroll
        Student student2 = new Student();
        student2.setFirstName("Jane");
        student2.setLastName("Smith");
        student2.setEmail("jane@email.com");
        student2.setPhone("9876543210");
        student2.setDateOfBirth("1996-01-01");
        student2.setSkillLevel(Student.SkillLevel.BEGINNER);
        student2 = studentDAO.save(student2);
        
        // Create another course
        Course course2 = new Course();
        course2.setName("Drawing 101");
        course2.setDescription("Basic Drawing");
        course2.setTerm(Course.Term.WINTER);
        course2.setSkillLevel(Student.SkillLevel.BEGINNER);
        course2.setInstructorId(testInstructor.getId());
        course2.setMaxCapacity(15);
        course2.setFee(new BigDecimal("149.99"));
        course2.setStartDate(LocalDate.now());
        course2.setEndDate(LocalDate.now().plusMonths(3));
        course2 = courseDAO.save(course2);
        
        Enrollment enrollment2 = controller.enrollStudent(student2.getId(), course2.getId());
        
        // Process one payment
        controller.processPayment(enrollment2.getId(), "Cash");
        
        var pendingPayments = controller.getPendingPayments();
        
        assertEquals(1, pendingPayments.size());
        assertTrue(pendingPayments.stream()
            .allMatch(p -> p.getStatus() == Payment.PaymentStatus.PENDING));
    }
}
