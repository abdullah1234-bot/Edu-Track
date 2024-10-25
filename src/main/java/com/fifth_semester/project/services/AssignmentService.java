package com.fifth_semester.project.services;

import com.fifth_semester.project.dtos.response.AssignmentDTO;
import com.fifth_semester.project.entities.*;
import com.fifth_semester.project.repositories.*;
import com.fifth_semester.project.security.services.UserDetailsImpl;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssignmentService {

    private static final Logger log = LoggerFactory.getLogger(AssignmentService.class);
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
    @Autowired
    private TeacherRepository teacherRepository;

    // Create a new assignment and notify all students in the section
    @Transactional
    public String createAssignmentForSection(String assignmentTitle, String description, String courseCode, String sectionName, LocalDate dueDate, String attachment) {
        log.info(" Hello World ");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Teacher teacher = teacherRepository.findByEmail(userDetails.getEmail()).orElseThrow(()->new RuntimeException("Teacher not found"));
        try {
            Course course = courseRepository.findByCourseCode(courseCode)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            Section section = sectionRepository.findBySectionNameAndCourseNameAndTeacherUsername(sectionName, course.getCourseName(), teacher.getUsername())
                    .orElseThrow(() -> new RuntimeException("Section not found"));

            // Get all enrolled students for the course in the specified section
            List<Student> students = enrollmentRepository.findStudentsByCourseIdAndSectionId(course.getId(), section.getId());

            for (Student student : students) {
                // Create assignment for each student
                Assignment assignment = new Assignment(
                        assignmentTitle,
                        description,
                        student,
                        LocalDate.now(),  // Upload date is now
                        dueDate,
                        false,  // Not yet submitted
                        false,  // Not yet graded
                        attachment
                );
                log.info("Number of students found: " + students.size());
                log.info("Sections: " + section.getSectionName());
                // Set the section explicitly
                assignment.setSection(section);

                // Log assignment details before saving
                log.info("Saving assignment for student: " + student.getUsername() + " with title: " + assignmentTitle);

                // Save the assignment and flush to force SQL execution
                try {
                    assignmentRepository.save(assignment);
                    assignmentRepository.flush(); // Ensure assignment is saved immediately
                    log.info("Assignment saved for student: " + student.getUsername());
                } catch (Exception e) {
                    System.err.println("Failed to save assignment for student " + student.getUsername() + ": " + e.getMessage());
                    e.printStackTrace();
                    continue;  // Skip to the next student if there is an error with this one
                }

                // Add assignment to student's assignment list (optional, for maintaining relationship)
                student.getAssignments().add(assignment);

                // Notify student about the new assignment
                try {
                    emailService.sendAssignmentNotification(student.getEmail(), student.getUsername(), assignmentTitle, dueDate);
                } catch (Exception e) {
                    System.err.println("Failed to send email notification to student " + student.getUsername() + ": " + e.getMessage());
                }

                // Save notification in the database
                try {
                    Notification notification = new Notification(
                            "New assignment created: " + assignment.getAssignmentTitle(),
                            NotificationType.ASSIGNMENT,
                            NotificationStatus.UNREAD,
                            student,
                            LocalDateTime.now()
                    );
                    notificationRepository.save(notification);
                } catch (Exception e) {
                    System.err.println("Failed to save notification for student " + student.getUsername() + ": " + e.getMessage());
                }
            }

            return "Assignment created for the section and notifications sent!";
        } catch (Exception e) {
            System.err.println("Exception occurred during assignment creation: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create assignment for the section", e);
        }
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
    public String submitAssignment(Student student, Long assignmentId, MultipartFile file) throws IOException {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        // Check if the student is enrolled in the course by querying the EnrollmentRepository
        Optional<Enrollment> enrollment = enrollmentRepository.findByStudentAndCourse(student,assignment.getSection().getCourse());
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
//    public List<Assignment> getAssignmentsForCourse(Long courseId) {
//        Course course = courseRepository.findById(courseId)
//                .orElseThrow(() -> new RuntimeException("Course not found"));
//        return assignmentRepository.findByCourse(course);
//    }
    public List<AssignmentDTO> getAssignmentsForCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

//        List<Assignment> assignments = assignmentRepository.findByCourse(course.getId());
//        return assignments.stream()
//                .map(assignment -> new AssignmentDTO(
//                        assignment.getId(),
//                        assignment.getAssignmentTitle(),
//                        assignment.getDescription(),
//                        assignment.getUploadDate(),
//                        assignment.getDueDate(),
//                        assignment.isSubmitted(),
//                        assignment.isGraded(),
//                        assignment.getAttachment(),
//                        assignment.getFeedback(),
//                        assignment.getMarks(),
//                        new AssignmentDTO.StudentInfoDTO(
//                                assignment.getStudent().getId(),
//                                assignment.getStudent().getUsername(),
//                                assignment.getStudent().getEmergencyContact()
//                        )
//                ))
//                .collect(Collectors.toList());
//
        List<Object[]> assignments = assignmentRepository.findByCourse(course.getId());
        return assignments.stream().map(result -> {
            AssignmentDTO assignmentDTO = new AssignmentDTO();
            assignmentDTO.setAssignment_id((Long) result[0]);
            assignmentDTO.setAssignmentTitle((String) result[1]);
            assignmentDTO.setDescription((String) result[2]);
            assignmentDTO.setUploadDate((Date) result[3]); // Convert to LocalDate
            assignmentDTO.setDueDate((Date) result[4]); // Convert to LocalDate
            assignmentDTO.setSubmitted((Boolean) result[5]);
            assignmentDTO.setGraded((Boolean) result[6]);
            assignmentDTO.setAttachment((String) result[7]);
            assignmentDTO.setFeedback((String) result[8]);
            assignmentDTO.setMarks((Integer) result[9]);

            AssignmentDTO.StudentInfoDTO studentInfo = new AssignmentDTO.StudentInfoDTO();
            studentInfo.setStudent_id((String) result[10]);
            studentInfo.setUsername((String) result[11]);
            studentInfo.setEmergencyContact((String) result[12]);

            assignmentDTO.setStudent(studentInfo);

            return assignmentDTO;
        }).collect(Collectors.toList());
    }

    // Get all assignments for a specific course and section with valid due dates
//    public List<Assignment> getValidAssignmentsForCourseAndSection(Long courseId, Long sectionId) {
//        Course course = courseRepository.findById(courseId)
//                .orElseThrow(() -> new RuntimeException("Course not found"));
//
//        Section section = sectionRepository.findById(sectionId)
//                .orElseThrow(() -> new RuntimeException("Section not found"));
//
//        LocalDate currentDate = LocalDate.now();  // Get current date
//
//        // Fetch assignments based on the repository method
//        return assignmentRepository.findByCourseAndSectionAndDueDate(course, section, currentDate);
//    }
    public List<AssignmentDTO> getValidAssignmentsForCourseAndStudent(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId).orElseThrow(()-> new RuntimeException("Course not found"));
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        List<Object[]> assignments = assignmentRepository.findBySectionCourseAndStudent(course.getId(),student.getId());
        return assignments.stream().map(result -> {
            AssignmentDTO assignmentDTO = new AssignmentDTO();
            assignmentDTO.setAssignment_id((Long) result[0]);
            assignmentDTO.setAssignmentTitle((String) result[1]);
            assignmentDTO.setDescription((String) result[2]);
            assignmentDTO.setUploadDate((Date) result[3]);
            assignmentDTO.setDueDate((Date) result[4]);
            assignmentDTO.setSubmitted((Boolean) result[5]);
            assignmentDTO.setGraded((Boolean) result[6]);
            assignmentDTO.setAttachment((String) result[7]);
            assignmentDTO.setFeedback((String) result[8]);
            assignmentDTO.setMarks((Integer) result[9]);
            AssignmentDTO.StudentInfoDTO studentInfo = new AssignmentDTO.StudentInfoDTO();
            studentInfo.setStudent_id((String) result[10]);
            studentInfo.setUsername((String) result[11]);
            studentInfo.setEmergencyContact((String) result[12]);
            assignmentDTO.setStudent(studentInfo);
            return assignmentDTO;
        }).collect(Collectors.toList());

    }
    public List<AssignmentDTO> getValidAssignmentsForCourseAndSection(Long courseId, String sectionName) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Section section = sectionRepository.findBySectionNameAndCourse(sectionName,course)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        List<Object[]> results = assignmentRepository.findBySectionAndDueDate(section.getId());

        return results.stream().map(result -> {
            AssignmentDTO assignmentDTO = new AssignmentDTO();
            assignmentDTO.setAssignment_id((Long) result[0]);
            assignmentDTO.setAssignmentTitle((String) result[1]);
            assignmentDTO.setDescription((String) result[2]);
            assignmentDTO.setUploadDate((Date) result[3]); // Convert to LocalDate
            assignmentDTO.setDueDate((Date) result[4]); // Convert to LocalDate
            assignmentDTO.setSubmitted((Boolean) result[5]);
            assignmentDTO.setGraded((Boolean) result[6]);
            assignmentDTO.setAttachment((String) result[7]);
            assignmentDTO.setFeedback((String) result[8]);
            assignmentDTO.setMarks((Integer) result[9]);

            AssignmentDTO.StudentInfoDTO studentInfo = new AssignmentDTO.StudentInfoDTO();
            studentInfo.setStudent_id((String) result[10]);
            studentInfo.setUsername((String) result[11]);
            studentInfo.setEmergencyContact((String) result[12]);

            assignmentDTO.setStudent(studentInfo);

            return assignmentDTO;
        }).collect(Collectors.toList());
    }

    // Method to convert java.sql.Date to java.time.LocalDate
    private LocalDate convertToLocalDate(Object date) {
        if (date instanceof java.sql.Date) {
            // Convert java.sql.Date to java.time.LocalDate
            return ((java.sql.Date) date).toLocalDate();
        } else if (date instanceof Date) {
            // Convert java.util.Date to java.time.LocalDate
            return ((Date) date).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return null;
    }
    // Scheduled task to check assignments due tomorrow and notify students who haven't submitted
    @Scheduled(cron = "0 1 0 * * *")  // Run every day at 00:01
    public void notifyStudentsOfPendingAssignments() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);  // Get tomorrow's date

        // Fetch all assignments due tomorrow
        List<Assignment> assignmentsDueTomorrow = assignmentRepository.findByDueDate(tomorrow);

        for (Assignment assignment : assignmentsDueTomorrow) {
            Course course = assignment.getSection().getCourse();
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
