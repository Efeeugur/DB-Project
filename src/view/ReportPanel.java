package view;

import controller.*;
import model.entity.Student;
import util.SwingUtils;
import javax.swing.*;
import java.awt.*;

/**
 * Panel for displaying reports.
 */
public class ReportPanel extends JPanel {
    
    private final StudentController studentController;
    private final InstructorController instructorController;
    private final CourseController courseController;
    private final EnrollmentController enrollmentController;
    
    private JPanel contentPanel;
    
    public ReportPanel(StudentController studentController,
                       InstructorController instructorController,
                       CourseController courseController,
                       EnrollmentController enrollmentController) {
        this.studentController = studentController;
        this.instructorController = instructorController;
        this.courseController = courseController;
        this.enrollmentController = enrollmentController;
        
        setLayout(new BorderLayout(20, 20));
        setBackground(SwingUtils.BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel title = SwingUtils.createTitleLabel("📊 Reports");
        add(title, BorderLayout.NORTH);
        
        // Content
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);
        
        // Refresh button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        JButton btnRefresh = SwingUtils.createPrimaryButton("Refresh");
        btnRefresh.addActionListener(e -> refresh());
        bottomPanel.add(btnRefresh);
        add(bottomPanel, BorderLayout.SOUTH);
        
        refresh();
    }
    
    public void refresh() {
        contentPanel.removeAll();
        
        // Summary Card
        JPanel summaryCard = SwingUtils.createCardPanel();
        summaryCard.setLayout(new BorderLayout(10, 10));
        summaryCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        
        JLabel summaryTitle = SwingUtils.createHeaderLabel("📈 System Summary");
        summaryCard.add(summaryTitle, BorderLayout.NORTH);
        
        JPanel statsGrid = new JPanel(new GridLayout(2, 3, 20, 20));
        statsGrid.setOpaque(false);
        
        statsGrid.add(createStatItem("Total Students", 
            String.valueOf(studentController.getStudentCount()), SwingUtils.PRIMARY_COLOR));
        statsGrid.add(createStatItem("Total Instructors", 
            String.valueOf(instructorController.getInstructorCount()), SwingUtils.SUCCESS_COLOR));
        statsGrid.add(createStatItem("Total Courses", 
            String.valueOf(courseController.getCourseCount()), SwingUtils.WARNING_COLOR));
        statsGrid.add(createStatItem("Active Enrollments", 
            String.valueOf(enrollmentController.getActiveEnrollments().size()), SwingUtils.SECONDARY_COLOR));
        statsGrid.add(createStatItem("Pending Payments", 
            String.valueOf(enrollmentController.getPendingPayments().size()), SwingUtils.DANGER_COLOR));
        
        summaryCard.add(statsGrid, BorderLayout.CENTER);
        contentPanel.add(summaryCard);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Students by Level Card
        JPanel levelCard = SwingUtils.createCardPanel();
        levelCard.setLayout(new BorderLayout(10, 10));
        levelCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        
        JLabel levelTitle = SwingUtils.createHeaderLabel("👨‍🎓 Students by Skill Level");
        levelCard.add(levelTitle, BorderLayout.NORTH);
        
        JPanel levelGrid = new JPanel(new GridLayout(1, 3, 20, 10));
        levelGrid.setOpaque(false);
        
        for (Student.SkillLevel level : Student.SkillLevel.values()) {
            int count = studentController.getStudentsByLevel(level).size();
            Color color = switch (level) {
                case BEGINNER -> new Color(46, 204, 113);
                case INTERMEDIATE -> new Color(241, 196, 15);
                case ADVANCED -> new Color(155, 89, 182);
            };
            levelGrid.add(createStatItem(level.toString(), String.valueOf(count), color));
        }
        
        levelCard.add(levelGrid, BorderLayout.CENTER);
        contentPanel.add(levelCard);
        contentPanel.add(Box.createVerticalStrut(20));
        
        // Quick Actions Card
        JPanel actionsCard = SwingUtils.createCardPanel();
        actionsCard.setLayout(new BorderLayout(10, 10));
        actionsCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        
        JLabel actionsTitle = SwingUtils.createHeaderLabel("⚡ Quick Stats");
        actionsCard.add(actionsTitle, BorderLayout.NORTH);
        
        StringBuilder statsText = new StringBuilder("<html>");
        statsText.append("<table style='font-size:12px;'>");
        statsText.append("<tr><td>• Students without enrollments:</td><td><b>")
            .append(countStudentsWithoutEnrollments()).append("</b></td></tr>");
        statsText.append("<tr><td>• Available courses (not full):</td><td><b>")
            .append(courseController.getAvailableCourses().size()).append("</b></td></tr>");
        statsText.append("</table></html>");
        
        JLabel statsLabel = new JLabel(statsText.toString());
        statsLabel.setFont(SwingUtils.LABEL_FONT);
        actionsCard.add(statsLabel, BorderLayout.CENTER);
        
        contentPanel.add(actionsCard);
        
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private JPanel createStatItem(String label, String value, Color color) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setOpaque(false);
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labelComponent.setForeground(Color.GRAY);
        
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueComponent.setForeground(color);
        
        panel.add(labelComponent, BorderLayout.NORTH);
        panel.add(valueComponent, BorderLayout.CENTER);
        
        return panel;
    }
    
    private int countStudentsWithoutEnrollments() {
        int count = 0;
        for (var student : studentController.getAllStudents()) {
            if (enrollmentController.getStudentEnrollments(student.getId()).isEmpty()) {
                count++;
            }
        }
        return count;
    }
}
