package com.fifth_semester.project.controllers;

import com.fifth_semester.project.dtos.response.TeacherDTO;
import com.fifth_semester.project.dtos.response.TeacherPerformanceDTO;
import com.fifth_semester.project.dtos.response.TeacherResponseDTO;
import com.fifth_semester.project.entities.Teacher;
import com.fifth_semester.project.entities.Course;
import com.fifth_semester.project.services.TeacherService;
import com.fifth_semester.project.dtos.response.MessageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/teachers")
@Tag(name = "Teacher APIs")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    // Endpoint to create a new teacher
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createTeacher(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String department,
            @RequestParam String officeHours,
            @RequestParam String qualification,
            @RequestParam String specialization,
            @RequestParam LocalDate dateOfHire) {

        try {
            String result = teacherService.createTeacher(username, email, department, officeHours, qualification, specialization, dateOfHire);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint to update an existing teacher
    @PutMapping("/update/{teacherId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateTeacher(
            @PathVariable Long teacherId,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String officeHours,
            @RequestParam(required = false) String qualification,
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) LocalDate dateOfHire) {

        try {
            String result = teacherService.updateTeacher(teacherId, username, email, department, officeHours, qualification, specialization, dateOfHire);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Endpoint to change password
    @PostMapping("/update/password/{teacherId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> changePassword(
            @PathVariable Long teacherId,
            @RequestParam String previousPassword,
            @RequestParam String newPassword) {

        try {
            String result = teacherService.changeTeacherPassword(teacherId, previousPassword, newPassword);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Assign teacher to a course
    @PostMapping("/assigncourse/{teacherId}/course/{courseId}/section/{sectionId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> assignTeacherToCourse(@PathVariable Long teacherId, @PathVariable Long courseId, @PathVariable Long sectionId) {
        String responseMessage = teacherService.assignTeacherToCourseAndSection(teacherId, courseId, sectionId);
        return ResponseEntity.ok(new MessageResponse(responseMessage));
    }

    // Get all teachers
    @GetMapping("/getinfoofall")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TeacherResponseDTO>> getAllTeachers() {
        List<TeacherResponseDTO> teachersInfo = teacherService.getAllTeachers(); // Returns DTOs
        return ResponseEntity.ok(teachersInfo);
    }


    // Get teacher by ID
    @GetMapping("/info/{teacherId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TEACHER')")
    public ResponseEntity<TeacherResponseDTO> getTeacherById(@PathVariable Long teacherId) {
        TeacherResponseDTO teacherInfo = teacherService.getTeacherById(teacherId);
        return ResponseEntity.ok(teacherInfo);
    }

    // Delete a teacher by ID
    @DeleteMapping("/delete/{teacherId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleteTeacher(@PathVariable Long teacherId) {
        teacherService.deleteTeacher(teacherId);
        return ResponseEntity.ok(new MessageResponse("Teacher deleted successfully"));
    }

    // Get courses assigned to a teacher
    @GetMapping("/{teacherId}/getassignedcourses")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<List<Course>> getCoursesByTeacher(@PathVariable Long teacherId) {
        List<Course> courses = teacherService.getCoursesByTeacher(teacherId);
        return ResponseEntity.ok(courses);
    }

    // Get teacher performance
    @GetMapping("/{teacherId}/performance")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<TeacherPerformanceDTO> getTeacherPerformance(@PathVariable Long teacherId) {
        TeacherPerformanceDTO performanceDTO = teacherService.getTeacherPerformance(teacherId);
        return ResponseEntity.ok(performanceDTO);
    }

    // Remove teacher from course
    @DeleteMapping("/{teacherId}/course/{courseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> removeTeacherFromCourse(@PathVariable Long teacherId, @PathVariable Long courseId) {
        String result = teacherService.removeTeacherFromCourse(teacherId, courseId);
        return ResponseEntity.ok(result);
    }
}
