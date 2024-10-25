package com.fifth_semester.project.controllers;

import com.fifth_semester.project.dtos.response.AssignmentFeedbackDTO;
import com.fifth_semester.project.dtos.response.GradeFeedbackDTO;
import com.fifth_semester.project.dtos.response.StudentDTO;
import com.fifth_semester.project.entities.*;
import com.fifth_semester.project.repositories.ParentRepository;
import com.fifth_semester.project.security.services.UserDetailsImpl;
import com.fifth_semester.project.services.ParentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/parent")
@Tag(name = "Parent APIs")
public class ParentController {

    @Autowired
    private ParentService parentService;
    @Autowired
    private ParentRepository parentRepository;

    // Get all children for a parent
    @GetMapping("/getchildren")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<List<StudentDTO>> getChildren() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Parent parent = parentRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new RuntimeException("Parent not found"));
        List<StudentDTO> children = parentService.getChildrenForParent(parent);
        return ResponseEntity.ok(children);
    }

    // Get academic progress (grades) for a specific child
    @GetMapping("/child/{studentId}/progress")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<List<Grade>> getAcademicProgressForChild(@PathVariable Long studentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Parent parent = parentRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new RuntimeException("Parent not found"));
        List<Grade> grades = parentService.getAcademicProgressForStudent(parent, studentId);
        return ResponseEntity.ok(grades);
    }

    // Get attendance for a specific child
    @GetMapping("/child/{studentId}/attendance")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<List<Attendance>> getAttendanceForChild(@PathVariable Long studentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Parent parent = parentRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new RuntimeException("Parent not found"));
        List<Attendance> attendance = parentService.getAttendanceForStudent(parent, studentId);
        return ResponseEntity.ok(attendance);
    }

    // Get teacher feedback from grades for a specific child
    @GetMapping("/child/{studentId}/feedback/grades")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<List<GradeFeedbackDTO>> getTeacherFeedbackFromGrades(@PathVariable Long studentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Parent parent = parentRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new RuntimeException("Parent not found"));
        List<Grade> feedbackFromGrades = parentService.getAcademicProgressForStudent(parent, studentId);

        List<GradeFeedbackDTO> feedbackDTOs = feedbackFromGrades.stream()
                .map(grade -> new GradeFeedbackDTO(grade.getExamType().toString(), grade.getMarks(), grade.getFeedback()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(feedbackDTOs);
    }

    // Get teacher feedback from assignments for a specific child
    @GetMapping("/child/{studentId}/feedback/assignments")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<List<AssignmentFeedbackDTO>> getTeacherFeedbackFromAssignments(@PathVariable Long studentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Parent parent = parentRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new RuntimeException("Parent not found"));
        List<Assignment> feedbackFromAssignments = parentService.getTeacherFeedbackFromAssignments(parent, studentId);
        List<AssignmentFeedbackDTO> feedbackDTOs = feedbackFromAssignments.stream()
                .map(assignment -> new AssignmentFeedbackDTO(
                        assignment.getAssignmentTitle(),
                        assignment.getMarks(),
                        assignment.getFeedback()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(feedbackDTOs);
    }
}
