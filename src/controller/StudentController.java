package controller;

import model.dao.StudentDAO;
import model.dao.SkillTestDAO;
import model.entity.Student;
import model.entity.SkillTest;
import java.util.List;
import java.util.Optional;

/**
 * Controller for Student operations.
 */
public class StudentController {
    
    private final StudentDAO studentDAO;
    private final SkillTestDAO skillTestDAO;
    
    public StudentController(StudentDAO studentDAO, SkillTestDAO skillTestDAO) {
        this.studentDAO = studentDAO;
        this.skillTestDAO = skillTestDAO;
    }
    
    /**
     * Registers a new student.
     */
    public Student registerStudent(String firstName, String lastName, String email, 
                                   String phone, String dateOfBirth) {
        // Check if email already exists
        if (studentDAO.findByEmail(email) != null) {
            throw new IllegalArgumentException("A student with this email already exists.");
        }
        
        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setEmail(email);
        student.setPhone(phone);
        student.setDateOfBirth(dateOfBirth);
        student.setSkillLevel(Student.SkillLevel.BEGINNER);
        
        return studentDAO.save(student);
    }
    
    /**
     * Gets a student by ID.
     */
    public Optional<Student> getStudentById(int id) {
        return studentDAO.findById(id);
    }
    
    /**
     * Gets all students.
     */
    public List<Student> getAllStudents() {
        return studentDAO.findAll();
    }
    
    /**
     * Updates student information.
     */
    public Student updateStudent(Student student) {
        return studentDAO.update(student);
    }
    
    /**
     * Deletes a student.
     */
    public boolean deleteStudent(int id) {
        return studentDAO.delete(id);
    }
    
    /**
     * Conducts a skill test for a student and updates their level.
     */
    public SkillTest conductSkillTest(int studentId, int score, String notes) {
        Optional<Student> studentOpt = studentDAO.findById(studentId);
        if (studentOpt.isEmpty()) {
            throw new IllegalArgumentException("Student not found.");
        }
        
        Student student = studentOpt.get();
        Student.SkillLevel newLevel = SkillTest.determineLevel(score);
        
        // Create skill test record
        SkillTest skillTest = new SkillTest();
        skillTest.setStudentId(studentId);
        skillTest.setScore(score);
        skillTest.setAssignedLevel(newLevel);
        skillTest.setNotes(notes);
        
        // Update student's skill level
        student.setSkillLevel(newLevel);
        studentDAO.update(student);
        
        return skillTestDAO.save(skillTest);
    }
    
    /**
     * Gets students by skill level.
     */
    public List<Student> getStudentsByLevel(Student.SkillLevel level) {
        return studentDAO.findBySkillLevel(level);
    }
    
    /**
     * Searches students by name.
     */
    public List<Student> searchStudents(String name) {
        return studentDAO.searchByName(name);
    }
    
    /**
     * Gets total student count.
     */
    public int getStudentCount() {
        return studentDAO.count();
    }
}
