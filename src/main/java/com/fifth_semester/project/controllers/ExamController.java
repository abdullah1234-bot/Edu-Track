package com.fifth_semester.project.controllers;

import com.fifth_semester.project.entities.Exam;
import com.fifth_semester.project.entities.ExamType;
import com.fifth_semester.project.services.ExamService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/exams")
@Tag(name = "Exam APIs")
public class ExamController {

    @Autowired
    private ExamService examService;

    // Get upcoming exams for a student
    @GetMapping("/student/{studentId}/upcoming")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getUpcomingExams(@PathVariable Long studentId) {
        List<Exam> exams = examService.getUpcomingExamsForStudent(studentId);
        return ResponseEntity.ok(exams);
    }

    // Get exams for a specific course
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('STUDENT')")
    public ResponseEntity<?> getExamsForCourse(@PathVariable Long courseId) {
        List<Exam> exams = examService.getExamsForCourse(courseId);
        return ResponseEntity.ok(exams);
    }

    // Schedule a new exam (Teachers/Admins)
    @PostMapping("/schedule")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> scheduleExam(@RequestParam String examId,
                                          @RequestParam ExamType examType,
                                          @RequestParam LocalDate examDate,
                                          @RequestParam String examLocation,
                                          @RequestParam int duration,
                                          @RequestParam Long courseId) {
        String result = examService.scheduleExam(examId, examType, examDate, examLocation, duration, courseId);
        return ResponseEntity.ok(result);
    }


}
