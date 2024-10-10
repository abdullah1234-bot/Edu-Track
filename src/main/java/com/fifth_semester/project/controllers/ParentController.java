package com.fifth_semester.project.controllers;

import com.fifth_semester.project.entities.*;
import com.fifth_semester.project.services.ParentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parent")
@Tag(name = "Parent APIs")
public class ParentController {

    @Autowired
    private ParentService parentService;

    // Get all children for a parent
    @GetMapping("/{parentId}/children")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<List<Student>> getChildren(@PathVariable Long parentId) {
        List<Student> children = parentService.getChildrenForParent(parentId);
        return ResponseEntity.ok(children);
    }

    // Get academic progress (grades) for a specific child
    @GetMapping("/{parentId}/child/{studentId}/progress")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<List<Grade>> getAcademicProgressForChild(
            @PathVariable Long parentId, @PathVariable Long studentId) {
        List<Grade> grades = parentService.getAcademicProgressForStudent(parentId, studentId);
        return ResponseEntity.ok(grades);
    }

    // Get attendance for a specific child
    @GetMapping("/{parentId}/child/{studentId}/attendance")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<List<Attendance>> getAttendanceForChild(
            @PathVariable Long parentId, @PathVariable Long studentId) {
        List<Attendance> attendance = parentService.getAttendanceForStudent(parentId, studentId);
        return ResponseEntity.ok(attendance);
    }

    // Get teacher feedback from grades for a specific child
    @GetMapping("/{parentId}/child/{studentId}/feedback/grades")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<List<Grade>> getTeacherFeedbackFromGrades(
            @PathVariable Long parentId, @PathVariable Long studentId) {
        List<Grade> feedbackFromGrades = parentService.getTeacherFeedbackFromGrades(parentId, studentId);
        return ResponseEntity.ok(feedbackFromGrades);
    }

    // Get teacher feedback from assignments for a specific child
    @GetMapping("/{parentId}/child/{studentId}/feedback/assignments")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<List<Assignment>> getTeacherFeedbackFromAssignments(
            @PathVariable Long parentId, @PathVariable Long studentId) {
        List<Assignment> feedbackFromAssignments = parentService.getTeacherFeedbackFromAssignments(parentId, studentId);
        return ResponseEntity.ok(feedbackFromAssignments);
    }
}
