package com.fifth_semester.project.controllers;

import com.fifth_semester.project.dtos.request.ClassScheduleReq;
import com.fifth_semester.project.dtos.response.ClassScheduleDTO;
import com.fifth_semester.project.dtos.response.MessageResponse;
import com.fifth_semester.project.dtos.response.TeacherClassScheduleDTO;
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

    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getStudentSchedules(@RequestParam Long studentId,
                                                 @RequestParam String day) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        List<ClassScheduleDTO> schedules = classScheduleService.getSchedulesForStudent(student, day);
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
        Teacher teacher = teacherRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        // Fetch schedules for the authenticated teacher
        List<TeacherClassScheduleDTO> schedules = classScheduleService.getSchedulesForTeacher(teacher);
        return ResponseEntity.ok(schedules);
    }

    // Get class schedules for a course
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> getCourseSchedules(@PathVariable Long courseId) {
        List<ClassScheduleDTO> schedules = classScheduleService.getSchedulesForCourse(courseId);
        return ResponseEntity.ok(schedules);
    }

    // Create or update a class schedule (for teachers/admins)
    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> saveClassSchedule(@Valid @RequestBody ClassScheduleReq classSchedule) {
        classScheduleService.saveClassSchedule(classSchedule);
        return ResponseEntity.ok(new MessageResponse("Class schedule saved successfully!"));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteclassschedule(@Valid @RequestBody ClassScheduleReq req) {
        classScheduleService.deleteclassschedule(req);
        return ResponseEntity.ok(new MessageResponse("Class schedule deleted successfully!"));

    }

}