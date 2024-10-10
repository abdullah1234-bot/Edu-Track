package com.fifth_semester.project.controllers;

import com.fifth_semester.project.dtos.response.MessageResponse;
import com.fifth_semester.project.entities.Grade;
import com.fifth_semester.project.services.GradeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
@Tag(name = "Grade APIs")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    // Endpoint for teachers to input or update a grade
    @PostMapping("/teacher/input")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> inputGrade(@RequestParam Long studentId,
                                        @RequestParam Long courseId,
                                        @RequestParam int marks,
                                        @RequestParam(required = false) String feedback) {
        String result = gradeService.addOrUpdateGrade(studentId, courseId, marks, feedback);
        return ResponseEntity.ok(new MessageResponse(result));
    }

    // Bulk upload endpoint for grades
    @PostMapping("/bulk-upload/{courseId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> bulkUploadGrades(@RequestParam("file") MultipartFile file,
                                              @PathVariable Long courseId) {
        try {
            String result = gradeService.bulkUploadGrades(file, courseId);
            return ResponseEntity.ok(new MessageResponse(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error uploading grades: " + e.getMessage()));
        }
    }

    // Endpoint to get section-wise grades for a specific course
    @GetMapping("/course/{courseId}/section/{sectionId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> viewGradesForCourseAndSection(@PathVariable Long courseId, @PathVariable Long sectionId) {
        List<Grade> grades = gradeService.getGradesForCourseAndSection(courseId, sectionId);
        return ResponseEntity.ok(grades);
    }

    // Endpoint for students to view their grades
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> viewGrades(@PathVariable Long studentId) {
        List<Grade> grades = gradeService.getGradesForStudent(studentId);
        return ResponseEntity.ok(grades);
    }

    // Endpoint to get grades for a specific course (admin or teacher)
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> viewGradesForCourse(@PathVariable Long courseId) {
        List<Grade> grades = gradeService.getGradesForCourse(courseId);
        return ResponseEntity.ok(grades);
    }
}
