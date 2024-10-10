package com.fifth_semester.project.services;

import com.fifth_semester.project.entities.*;
import com.fifth_semester.project.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SectionRepository sectionRepository;

    private final String assignmentStoragePath = "uploads/assignments/";

    // Create a new assignment and notify all students in the section
    public String createAssignmentForSection(String assignmentTitle, String description, Long courseId, Long sectionId, LocalDate dueDate, String attachment) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        // Get all enrolled students for the course in the specified section
        List<Student> students = enrollmentRepository.findStudentsByCourseIdAndSectionId(courseId, sectionId);

        for (Student student : students) {
            // Create assignment for each student
            Assignment assignment = new Assignment(
                    assignmentTitle,
                    description,
                    course,
                    student,
                    LocalDate.now(),  // Upload date is now
                    dueDate,
                    false,  // Not yet submitted
                    false,  // Not yet graded
                    attachment
            );
            assignmentRepository.save(assignment);

            // Notify student about the new assignment
            emailService.sendAssignmentNotification(student.getEmail(), student.getUsername(), assignmentTitle, dueDate);

            // Save notification in the database
            Notification notification = new Notification(
                    "New assignment created: " + assignment.getAssignmentTitle(),
                    NotificationType.ASSIGNMENT,
                    NotificationStatus.UNREAD,
                    student,
                    LocalDateTime.now()
            );
            notificationRepository.save(notification);
        }

        return "Assignment created for the section and notifications sent!";
    }

    // Teacher grades an assignment and notifies the student
    public String gradeAssignment(Long assignmentId, int marks, String feedback) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        // Update assignment grade and feedback
        assignment.setGraded(true);
        assignment.setSubmitted(true);  // Assuming grading implies it was submitted
        assignment.setMarks(marks);
        assignment.setFeedback(feedback);

        assignmentRepository.save(assignment);

        // Notify the student of their grade
        Student student = assignment.getStudent();
        emailService.sendAssignmentGradedNotification(student.getEmail(), student.getUsername(), assignment.getAssignmentTitle(), marks, feedback);
        // Save notification in the database
        Notification notification = new Notification(
                "Your assignment '" + assignment.getAssignmentTitle() + "' has been graded.",
                NotificationType.GRADE_RELEASED,
                NotificationStatus.UNREAD,
                student,
                LocalDateTime.now()
        );
        notificationRepository.save(notification);

        // Notify the parent if they exist
        Parent parent = student.getParent();
        if (parent != null) {
            emailService.sendAssignmentGradedNotificationToParent(
                    parent.getEmail(),
                    parent.getUsername(),
                    student.getUsername(),
                    assignment.getAssignmentTitle(),
                    marks,
                    feedback
            );

            // Save notification for parent
            Notification parentNotification = new Notification(
                    "Your child " + student.getUsername() + "'s assignment '" + assignment.getAssignmentTitle() + "' has been graded.",
                    NotificationType.GRADE_RELEASED,
                    NotificationStatus.UNREAD,
                    parent,
                    LocalDateTime.now()
            );
            notificationRepository.save(parentNotification);
        }
        return "Assignment graded and student notified.";
    }

    // Method to upload assignment file by students
    public String submitAssignment(Long studentId, Long assignmentId, MultipartFile file) throws IOException {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        // Check if the student is enrolled in the course by querying the EnrollmentRepository
        Optional<Enrollment> enrollment = enrollmentRepository.findByStudentAndCourse(student, assignment.getCourse());
        if (enrollment.isEmpty()) {
            return "Student is not enrolled in this course!";
        }

        // Save the uploaded file
        String fileName = saveFile(file);
        assignment.setAttachment(fileName);
        assignment.setSubmitted(true);
        assignment.setStudent(student);

        assignmentRepository.save(assignment);

        return "Assignment submitted successfully!";
    }

    // Save assignment file
    private String saveFile(MultipartFile file) throws IOException {
        Path filePath = Paths.get(assignmentStoragePath, file.getOriginalFilename());
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());
        return filePath.toString();
    }

    // Get all assignments for a specific course
    public List<Assignment> getAssignmentsForCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        return assignmentRepository.findByCourse(course);
    }

    // Get all assignments for a specific course and section with valid due dates
    public List<Assignment> getValidAssignmentsForCourseAndSection(Long courseId, Long sectionId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        LocalDate currentDate = LocalDate.now();  // Get current date

        // Fetch assignments based on the repository method
        return assignmentRepository.findByCourseAndSectionAndDueDate(course, section, currentDate);
    }

    // Scheduled task to check assignments due tomorrow and notify students who haven't submitted
    @Scheduled(cron = "0 1 0 * * *")  // Run every day at 00:01
    public void notifyStudentsOfPendingAssignments() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);  // Get tomorrow's date

        // Fetch all assignments due tomorrow
        List<Assignment> assignmentsDueTomorrow = assignmentRepository.findByDueDate(tomorrow);

        for (Assignment assignment : assignmentsDueTomorrow) {
            Course course = assignment.getCourse();
            List<Student> students = enrollmentRepository.findStudentsByCourseId(course.getId());

            for (Student student : students) {
                // If the assignment is not submitted by the student
                if (!assignment.isSubmitted()) {
                    // Send email notification
                    emailService.sendAssignmentReminder(student.getEmail(), student.getUsername(), assignment.getAssignmentTitle(), assignment.getDueDate());

                    // Create and save notification in the database
                    Notification notification = new Notification(
                            "Reminder: Your assignment '" + assignment.getAssignmentTitle() + "' is due tomorrow.",
                            NotificationType.ASSIGNMENT_DEADLINE_REMINDER,
                            NotificationStatus.UNREAD,
                            student,
                            LocalDateTime.now()
                    );
                    notificationRepository.save(notification);
                }
            }
        }
    }
}
