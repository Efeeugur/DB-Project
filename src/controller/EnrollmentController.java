package controller;

import model.dao.CourseDAO;
import model.dao.EnrollmentDAO;
import model.dao.PaymentDAO;
import model.dao.StudentDAO;
import model.entity.Course;
import model.entity.Enrollment;
import model.entity.Payment;
import model.entity.Student;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Controller for Enrollment operations.
 */
public class EnrollmentController {
    
    private final EnrollmentDAO enrollmentDAO;
    private final StudentDAO studentDAO;
    private final CourseDAO courseDAO;
    private final PaymentDAO paymentDAO;
    
    public EnrollmentController(EnrollmentDAO enrollmentDAO, StudentDAO studentDAO, 
                                CourseDAO courseDAO, PaymentDAO paymentDAO) {
        this.enrollmentDAO = enrollmentDAO;
        this.studentDAO = studentDAO;
        this.courseDAO = courseDAO;
        this.paymentDAO = paymentDAO;
    }
    
    /**
     * Enrolls a student in a course.
     */
    public Enrollment enrollStudent(int studentId, int courseId) {
        // Validate student exists
        Optional<Student> studentOpt = studentDAO.findById(studentId);
        if (studentOpt.isEmpty()) {
            throw new IllegalArgumentException("Student not found.");
        }
        
        // Validate course exists
        Optional<Course> courseOpt = courseDAO.findById(courseId);
        if (courseOpt.isEmpty()) {
            throw new IllegalArgumentException("Course not found.");
        }
        
        Student student = studentOpt.get();
        Course course = courseOpt.get();
        
        // Check if student skill level matches course level
        if (student.getSkillLevel() != course.getSkillLevel()) {
            throw new IllegalArgumentException(
                "Student skill level (" + student.getSkillLevel() + 
                ") does not match course level (" + course.getSkillLevel() + ").");
        }
        
        // Check if already enrolled
        if (enrollmentDAO.findByStudentAndCourse(studentId, courseId) != null) {
            throw new IllegalArgumentException("Student is already enrolled in this course.");
        }
        
        // Check course capacity
        int currentEnrollments = enrollmentDAO.countByCourseId(courseId);
        if (currentEnrollments >= course.getMaxCapacity()) {
            throw new IllegalArgumentException("Course is full.");
        }
        
        // Create enrollment
        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(studentId);
        enrollment.setCourseId(courseId);
        enrollment.setStatus(Enrollment.Status.ACTIVE);
        
        Enrollment savedEnrollment = enrollmentDAO.save(enrollment);
        
        // Create pending payment
        Payment payment = new Payment();
        payment.setEnrollmentId(savedEnrollment.getId());
        payment.setAmount(course.getFee());
        payment.setStatus(Payment.PaymentStatus.PENDING);
        paymentDAO.save(payment);
        
        return savedEnrollment;
    }
    
    /**
     * Drops a student from a course.
     */
    public boolean dropEnrollment(int enrollmentId) {
        Optional<Enrollment> enrollmentOpt = enrollmentDAO.findById(enrollmentId);
        if (enrollmentOpt.isEmpty()) {
            return false;
        }
        
        Enrollment enrollment = enrollmentOpt.get();
        enrollment.setStatus(Enrollment.Status.DROPPED);
        enrollmentDAO.update(enrollment);
        return true;
    }
    
    /**
     * Completes an enrollment.
     */
    public boolean completeEnrollment(int enrollmentId) {
        Optional<Enrollment> enrollmentOpt = enrollmentDAO.findById(enrollmentId);
        if (enrollmentOpt.isEmpty()) {
            return false;
        }
        
        Enrollment enrollment = enrollmentOpt.get();
        enrollment.setStatus(Enrollment.Status.COMPLETED);
        enrollmentDAO.update(enrollment);
        return true;
    }
    
    /**
     * Gets enrollments for a student.
     */
    public List<Enrollment> getStudentEnrollments(int studentId) {
        return enrollmentDAO.findByStudentId(studentId);
    }
    
    /**
     * Gets enrollments for a course.
     */
    public List<Enrollment> getCourseEnrollments(int courseId) {
        return enrollmentDAO.findByCourseId(courseId);
    }
    
    /**
     * Gets all active enrollments.
     */
    public List<Enrollment> getActiveEnrollments() {
        return enrollmentDAO.findActiveEnrollments();
    }
    
    /**
     * Processes payment for an enrollment.
     */
    public Payment processPayment(int enrollmentId, String paymentMethod) {
        List<Payment> payments = paymentDAO.findByEnrollmentId(enrollmentId);
        
        for (Payment payment : payments) {
            if (payment.getStatus() == Payment.PaymentStatus.PENDING) {
                payment.setPaymentMethod(paymentMethod);
                payment.setStatus(Payment.PaymentStatus.COMPLETED);
                return paymentDAO.update(payment);
            }
        }
        
        throw new IllegalArgumentException("No pending payment found for this enrollment.");
    }
    
    /**
     * Gets pending payments.
     */
    public List<Payment> getPendingPayments() {
        return paymentDAO.findPendingPayments();
    }
}
