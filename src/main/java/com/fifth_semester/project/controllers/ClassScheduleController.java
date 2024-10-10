package com.fifth_semester.project.controllers;

import com.fifth_semester.project.dtos.response.MessageResponse;
import com.fifth_semester.project.entities.ClassSchedule;
import com.fifth_semester.project.entities.Student;
import com.fifth_semester.project.entities.Teacher;
import com.fifth_semester.project.repositories.StudentRepository;
import com.fifth_semester.project.repositories.TeacherRepository;
import com.fifth_semester.project.security.services.UserDetailsImpl;
import com.fifth_semester.project.services.ClassScheduleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@Tag(name = "Class Scheduling APIs")
public class ClassScheduleController {

    @Autowired
    private ClassScheduleService classScheduleService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    // Get class schedules for a student
    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getStudentSchedules(@RequestParam Long studentId,
                                                 @RequestParam LocalDate startDate,
                                                 @RequestParam LocalDate endDate) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<ClassSchedule> schedules = classScheduleService.getSchedulesForStudent(student, startDate, endDate);
        return ResponseEntity.ok(schedules);
    }

    // Get class schedules for a teacher
    @GetMapping("/teacher")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> getTeacherSchedules() {
        // Get the authenticated teacher's details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Fetch teacher from the repository by username or email (depending on your implementation)
        Teacher teacher = teacherRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        // Fetch schedules for the authenticated teacher
        List<ClassSchedule> schedules = classScheduleService.getSchedulesForTeacher(teacher);
        return ResponseEntity.ok(schedules);
    }

    // Get class schedules for a course
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> getCourseSchedules(@PathVariable Long courseId) {
        List<ClassSchedule> schedules = classScheduleService.getSchedulesForCourse(courseId);
        return ResponseEntity.ok(schedules);
    }

    // Create or update a class schedule (for teachers/admins)
    @PostMapping("/save")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> saveClassSchedule(@Valid @RequestBody ClassSchedule classSchedule) {
        classScheduleService.saveClassSchedule(classSchedule);
        return ResponseEntity.ok(new MessageResponse("Class schedule saved successfully!"));
    }
}

