package com.fifth_semester.project.controllers;

import com.fifth_semester.project.dtos.response.AssignmentDTO;
import com.fifth_semester.project.dtos.response.MessageResponse;
import com.fifth_semester.project.entities.Assignment;
import com.fifth_semester.project.entities.Student;
import com.fifth_semester.project.repositories.StudentRepository;
import com.fifth_semester.project.security.services.UserDetailsImpl;
import com.fifth_semester.project.services.AssignmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/assignments")
@Tag(name = "Assignment APIs")
public class AssignmentController {
    @Autowired
    private AssignmentService assignmentService;
    @Autowired
    private StudentRepository studentRepository;

    // Endpoint for teacher to create an assignment for a specific section
    @PostMapping("/create")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> createAssignmentForSection(@RequestParam String assignmentTitle,
                                                        @RequestParam String description,
                                                        @RequestParam String courseCode,
                                                        @RequestParam String sectionName,
                                                        @RequestParam String attachment,
                                                        @RequestParam LocalDate dueDate) {
        String result = assignmentService.createAssignmentForSection(assignmentTitle, description, courseCode, sectionName, dueDate, attachment);
        return ResponseEntity.ok(new MessageResponse(result));
    }

    // Endpoint to get all assignments for a specific course
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> getAssignmentsForCourse(@PathVariable Long courseId) {
        List<AssignmentDTO> assignments = assignmentService.getAssignmentsForCourse(courseId);
        return ResponseEntity.ok(assignments);
    }
//    public ResponseEntity<?> getAssignmentsForCourse(@PathVariable Long courseId) {
//        List<Assignment> assignments = assignmentService.getAssignmentsForCourse(courseId);
//        return ResponseEntity.ok(assignments);
//    }

    // Endpoint to get valid assignments for a specific course and section
    @GetMapping("/course/{courseId}/section/{sectionName}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER')")
    public ResponseEntity<?> getValidAssignmentsForCourseAndSection(@PathVariable Long courseId, @PathVariable String sectionName) {
        List<AssignmentDTO> assignments = assignmentService.getValidAssignmentsForCourseAndSection(courseId, sectionName);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/course/{courseId}/student/{studentId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER')")
    public ResponseEntity<?> getValidAssignmentsForCourseAndStudent(@PathVariable Long courseId, @PathVariable Long studentId) {
        List<AssignmentDTO> assignments = assignmentService.getValidAssignmentsForCourseAndStudent(courseId, studentId);
        return ResponseEntity.ok(assignments);
    }

    // Endpoint for teacher to grade an assignment
    @PostMapping("/grade")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> gradeAssignment(@RequestParam Long assignmentId, @RequestParam int marks, @RequestParam String feedback) {
        String result = assignmentService.gradeAssignment(assignmentId, marks, feedback);
        return ResponseEntity.ok(new MessageResponse(result));
    }

    // Endpoint for student to submit an assignment
    @PostMapping("/submit")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> submitAssignment(@RequestParam Long assignmentId, @RequestParam("file") MultipartFile file) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Student student = studentRepository.findByEmail(userDetails.getEmail()).orElse(null);
            String result = assignmentService.submitAssignment(student, assignmentId, file);
            return ResponseEntity.ok(new MessageResponse(result));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Failed to upload the assignment."));
        }
    }


}
