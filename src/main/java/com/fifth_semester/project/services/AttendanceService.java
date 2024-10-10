package com.fifth_semester.project.services;

import com.fifth_semester.project.dtos.request.AttendanceData;
import com.fifth_semester.project.entities.*;
import com.fifth_semester.project.repositories.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SectionRepository sectionRepository;

// Method to mark attendance for a student in a course using enrollment
    public String markAttendance(Long studentId, Long courseId, LocalDate attendanceDate, boolean isPresent) {
        Optional<Enrollment> enrollmentOpt = enrollmentRepository.findByStudentIdAndCourseId(studentId, courseId);
        if (enrollmentOpt.isEmpty()) {
            throw new RuntimeException("Enrollment not found for student ID: " + studentId + " and course ID: " + courseId);
        }

        Enrollment enrollment = enrollmentOpt.get();
        Attendance attendance = new Attendance(enrollment, attendanceDate, isPresent);
        attendanceRepository.save(attendance);

        return "Attendance marked successfully!";
    }

    // Method to get attendance records for a student by student ID
    public List<Attendance> getAttendanceByStudent(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }

    // Method to get attendance records for a course by course ID
    public List<Attendance> getAttendanceByCourse(Long courseId) {
        return attendanceRepository.findByCourseId(courseId);
    }

    // Retrieve attendance by student and course
    public List<Attendance> getAttendanceByStudentAndCourse(Long studentId, Long courseId) {
        Student student = new Student(); // Assuming you have access to student entity somehow
        Course course = new Course(); // Assuming you have access to course entity somehow
        return attendanceRepository.findByStudentAndCourse(student, course);
    }

    // Retrieve section-wise attendance for a specific course using course and section
    public List<Attendance> getSectionWiseAttendance(Long sectionId, Long courseId) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        return attendanceRepository.findByCourseAndSection(course, section);
    }

    // Retrieve attendance by student, course, and date
    public Optional<Attendance> getAttendanceByStudentCourseAndDate(Long studentId, Long courseId, LocalDate attendanceDate) {
        Student student = new Student(); // Assuming you have access to student entity
        Course course = new Course(); // Assuming you have access to course entity
        return attendanceRepository.findByStudentAndCourseAndAttendanceDate(student, course, attendanceDate);
    }

    // Method for bulk upload of attendance for a section using enrollment
    @Transactional
    public String uploadBulkAttendance(Long sectionId, Long courseId, List<AttendanceData> attendanceDataList, LocalDate attendanceDate) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        for (AttendanceData data : attendanceDataList) {
            Optional<Enrollment> enrollmentOpt = enrollmentRepository.findByStudentIdAndCourseId(data.getStudentId(), courseId);
            if (enrollmentOpt.isPresent()) {
                Enrollment enrollment = enrollmentOpt.get();

                // Check if attendance for this enrollment already exists on the same day
                Optional<Attendance> existingAttendance = attendanceRepository.findByStudentAndCourseAndAttendanceDate(enrollment.getStudent(), enrollment.getCourse(), attendanceDate);

                if (existingAttendance.isPresent()) {
                    // Update the existing attendance record
                    Attendance attendance = existingAttendance.get();
                    attendance.setPresent(data.isPresent());
                    attendanceRepository.save(attendance);
                } else {
                    // Create new attendance record
                    Attendance attendance = new Attendance(enrollment, attendanceDate, data.isPresent());
                    attendanceRepository.save(attendance);
                }
            } else {
                throw new RuntimeException("Enrollment not found for student ID: " + data.getStudentId() + " and course ID: " + courseId);
            }
        }

        return "Bulk attendance uploaded successfully for section " + section.getSectionName();
    }
}