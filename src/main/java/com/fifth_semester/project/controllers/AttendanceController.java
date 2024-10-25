package com.fifth_semester.project.controllers;

import com.fifth_semester.project.dtos.request.AttendanceData;
import com.fifth_semester.project.dtos.response.CourseAndSectionAttendanceDTO;
import com.fifth_semester.project.dtos.response.MessageResponse;
import com.fifth_semester.project.dtos.response.StudentAttendanceResponseDTO;
import com.fifth_semester.project.entities.Attendance;
import com.fifth_semester.project.entities.Teacher;
import com.fifth_semester.project.repositories.TeacherRepository;
import com.fifth_semester.project.security.services.UserDetailsImpl;
import com.fifth_semester.project.services.AttendanceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/attendance")
@Tag(name = "Attendance APIs")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private TeacherRepository teacherRepository;
    // Endpoint for teachers to mark attendance for a student
    @PostMapping("/mark")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> markAttendance(@RequestParam String studentId,
                                            @RequestParam String courseName,
                                            @RequestParam LocalDate attendanceDate,
                                            @RequestParam boolean isPresent) {
        // Get the authenticated teacher's details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Fetch teacher from the repository by username or email (depending on your implementation)
        Teacher teacher = teacherRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        String result = attendanceService.markAttendance(studentId, courseName, attendanceDate, isPresent, teacher);

        return ResponseEntity.ok(new MessageResponse(result));
    }

    // Endpoint to get attendance records for a student
    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<?> getStudentAttendance(@PathVariable Long studentId) {
        List<StudentAttendanceResponseDTO> attendances = attendanceService.getAttendanceByStudent(studentId);
        return ResponseEntity.ok(attendances);
    }

    // Endpoint to get attendance records for a course (e.g., for teacher/admin view)
    @GetMapping("/course/{courseName}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> getCourseAttendance(@PathVariable String courseName) {
        List<Attendance> attendances = attendanceService.getAttendanceByCourse(courseName);
        return ResponseEntity.ok(attendances);
    }

    // Get attendance by student and course
    @GetMapping("/student/{studentId}/course/{courseId}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<List<Attendance>> getAttendanceByStudentAndCourse(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        List<Attendance> attendanceList = attendanceService.getAttendanceByStudentAndCourse(studentId, courseId);
        return ResponseEntity.ok(attendanceList);
    }

    // Get attendance by student, course, and date
    @GetMapping("/student/{studentId}/course/{courseId}/date")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Optional<Attendance>> getAttendanceByStudentCourseAndDate(
            @PathVariable Long studentId,
            @PathVariable Long courseId,
            @RequestParam String attendanceDate) {

        LocalDate date = LocalDate.parse(attendanceDate);
        Optional<Attendance> attendance = attendanceService.getAttendanceByStudentCourseAndDate(studentId, courseId, date);
        return ResponseEntity.ok(attendance);
    }


    // Endpoint to retrieve section-wise attendance
    @GetMapping("/section")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> getSectionWiseAttendance(@RequestParam Long sectionId, @RequestParam Long courseId) {
        List<CourseAndSectionAttendanceDTO> attendances = attendanceService.getSectionWiseAttendance(sectionId, courseId);
        return ResponseEntity.ok(attendances);
    }

    //bulk-upload attendance
    @PostMapping("/upload-bulk")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> uploadBulkAttendance(@RequestParam @NotNull @Min(1) Long sectionId, @RequestParam @NotNull @Min(1) Long courseId,
                                                  @RequestParam @NotNull LocalDate attendanceDate,
                                                  @Valid @RequestBody List<@Valid AttendanceData> attendanceDataList) {
//        for(AttendanceData dt: attendanceDataList){
//            System.out.println("Student id : "+ dt.getStudentId());
//            System.out.println("Present Status: "+dt.getStatus());
//        }
        String result = attendanceService.uploadBulkAttendance(sectionId, courseId, attendanceDataList, attendanceDate);
        return ResponseEntity.ok(result);
    }

}
