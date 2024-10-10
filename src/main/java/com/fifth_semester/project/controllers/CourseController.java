package com.fifth_semester.project.controllers;

import com.fifth_semester.project.dtos.request.CreateCourseDTO;
import com.fifth_semester.project.dtos.response.MessageResponse;
import com.fifth_semester.project.entities.Course;
import com.fifth_semester.project.entities.Student;
import com.fifth_semester.project.exception.ResourceNotFoundException;
import com.fifth_semester.project.repositories.CourseRepository;
import com.fifth_semester.project.repositories.StudentRepository;
import com.fifth_semester.project.services.CourseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@Tag(name = "Course APIs")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createCourse(@Valid @RequestBody CreateCourseDTO courseDTO) {
        try {
            Course createdCourse = courseService.createCourse(courseDTO);
            return ResponseEntity.ok(createdCourse);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: Unable to create course. " + e.getMessage());
        }
    }

    @GetMapping("/available")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getAvailableCourses(@RequestParam Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<Course> availableCourses = courseService.getAvailableCoursesForStudent(student);
        return ResponseEntity.ok(availableCourses);
    }

    @PostMapping("/enroll")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> enrollInCourse(@RequestParam Long studentId, @RequestParam Long courseId, @RequestParam boolean isBacklog) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        String result = courseService.enrollStudentInCourse(student, course, isBacklog);
        return ResponseEntity.ok(new MessageResponse(result));
    }
}
