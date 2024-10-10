package com.fifth_semester.project.controllers;

import com.fifth_semester.project.dtos.request.AttendanceData;
import com.fifth_semester.project.dtos.response.MessageResponse;
import com.fifth_semester.project.entities.Attendance;
import com.fifth_semester.project.services.AttendanceService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/attendance")
@Tag(name = "Attendance APIs")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    // Endpoint for teachers to mark attendance for a student
    @PostMapping("/mark")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> markAttendance(@RequestParam Long studentId,
                                            @RequestParam Long courseId,
                                            @RequestParam LocalDate attendanceDate,
                                            @RequestParam boolean isPresent) {
        String result = attendanceService.markAttendance(studentId, courseId, attendanceDate, isPresent);
        return ResponseEntity.ok(new MessageResponse(result));
    }

    // Endpoint to get attendance records for a student
    @GetMapping("/student")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> getStudentAttendance(@RequestParam Long studentId) {
        List<Attendance> attendances = attendanceService.getAttendanceByStudent(studentId);
        return ResponseEntity.ok(attendances);
    }

    // Endpoint to get attendance records for a course (e.g., for teacher/admin view)
    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    public ResponseEntity<?> getCourseAttendance(@PathVariable Long courseId) {
        List<Attendance> attendances = attendanceService.getAttendanceByCourse(courseId);
        return ResponseEntity.ok(attendances);
    }

    // Get attendance by student and course
    @GetMapping("/student/{studentId}/course/{courseId}")
    public ResponseEntity<List<Attendance>> getAttendanceByStudentAndCourse(
            @PathVariable Long studentId,
            @PathVariable Long courseId) {
        List<Attendance> attendanceList = attendanceService.getAttendanceByStudentAndCourse(studentId, courseId);
        return ResponseEntity.ok(attendanceList);
    }

    // Get attendance by student, course, and date
    @GetMapping("/student/{studentId}/course/{courseId}/date")
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
        List<Attendance> attendances = attendanceService.getSectionWiseAttendance(sectionId, courseId);
        return ResponseEntity.ok(attendances);
    }

    //bulk-upload attendance
    @PostMapping("/upload-bulk")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<?> uploadBulkAttendance(@RequestParam Long sectionId, @RequestParam Long courseId,
                                                  @RequestParam LocalDate attendanceDate,
                                                  @RequestBody List<AttendanceData> attendanceDataList) {
        String result = attendanceService.uploadBulkAttendance(sectionId, courseId, attendanceDataList, attendanceDate);
        return ResponseEntity.ok(result);
    }

}
