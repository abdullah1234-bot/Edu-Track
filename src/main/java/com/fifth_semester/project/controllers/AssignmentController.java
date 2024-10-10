package com.fifth_semester.project.controllers;

import com.fifth_semester.project.dtos.response.MessageResponse;
import com.fifth_semester.project.entities.Assignment;
import com.fifth_semester.project.services.AssignmentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    // Endpoint for teacher to create an assignment for a specific section
    @PostMapping("/create")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> createAssignmentForSection(@RequestParam String assignmentTitle,
                                                        @RequestParam String description,
                                                        @RequestParam Long courseId,
                                                        @RequestParam Long sectionId,
                                                        @RequestParam String attachment,
                                                        @RequestParam LocalDate dueDate) {
        String result = assignmentService.createAssignmentForSection(assignmentTitle, description, courseId, sectionId, dueDate, attachment);
        return ResponseEntity.ok(new MessageResponse(result));
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
    public ResponseEntity<?> submitAssignment(@RequestParam Long studentId, @RequestParam Long assignmentId, @RequestParam("file") MultipartFile file) {
        try {
            String result = assignmentService.submitAssignment(studentId, assignmentId, file);
            return ResponseEntity.ok(new MessageResponse(result));
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Failed to upload the assignment."));
        }
    }

    // Endpoint to get all assignments for a specific course
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER')")
    public ResponseEntity<?> getAssignmentsForCourse(@PathVariable Long courseId) {
        List<Assignment> assignments = assignmentService.getAssignmentsForCourse(courseId);
        return ResponseEntity.ok(assignments);
    }

    // Endpoint to get valid assignments for a specific course and section
    @GetMapping("/course/{courseId}/section/{sectionId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('TEACHER')")
    public ResponseEntity<?> getValidAssignmentsForCourseAndSection(@PathVariable Long courseId, @PathVariable Long sectionId) {
        List<Assignment> assignments = assignmentService.getValidAssignmentsForCourseAndSection(courseId, sectionId);
        return ResponseEntity.ok(assignments);
    }
}
