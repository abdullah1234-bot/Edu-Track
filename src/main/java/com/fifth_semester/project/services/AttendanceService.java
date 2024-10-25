package com.fifth_semester.project.services;

import com.fifth_semester.project.dtos.request.AttendanceData;
import com.fifth_semester.project.dtos.response.CourseAndSectionAttendanceDTO;
import com.fifth_semester.project.dtos.response.StudentAttendanceResponseDTO;
import com.fifth_semester.project.entities.*;
import com.fifth_semester.project.repositories.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import com.fifth_semester.project.entities.Teacher;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SectionRepository sectionRepository;

    private static final Logger logger = LoggerFactory.getLogger(AttendanceService.class);
// Method to mark attendance for a student in a course using enrollment
    public String markAttendance(String studentId, String courseName, LocalDate attendanceDate, boolean isPresent, Teacher teacher) {
        Optional<Enrollment> enrollmentOpt = enrollmentRepository.findByStudentStudentIdAndCourseCourseName(studentId, courseName);

        if (enrollmentOpt.isEmpty()) {
            throw new RuntimeException("Enrollment not found for student ID: " + studentId + " and course Name: " + courseName);
        }

        Enrollment enrollment = enrollmentOpt.get();
        Attendance attendance = new Attendance(enrollment, attendanceDate, isPresent);
        attendanceRepository.save(attendance);

        return "Attendance marked successfully!";
    }

    // Method to get attendance records for a student by student ID
    public List<StudentAttendanceResponseDTO> getAttendanceByStudent(Long studentId) {
        return attendanceRepository.findByStudentIdwithEnrollmentDetails(studentId);
    }

    // Method to get attendance records for a course by course ID
    public List<Attendance> getAttendanceByCourse(String courseName) {
        return attendanceRepository.findByEnrollmentCourseCourseName(courseName);
    }

    // Retrieve attendance by student and course
    public List<Attendance> getAttendanceByStudentAndCourse (Long studentId, Long courseId) throws RuntimeException {
//        Optional<Student> studentOpt = studentRepository.findById(studentId);
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + studentId));

            // Fetch the course entity or throw exception
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + courseId));

            // Fetch the enrollment entity or throw exception
            Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(student.getId(), course.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Enrollment not found for Student ID: " + studentId + " and Course ID: " + courseId));

            // Fetch and return attendance records
            return attendanceRepository.findByEnrollment(enrollment);
    }

    // Retrieve section-wise attendance for a specific course using course and section
    public List<CourseAndSectionAttendanceDTO> getSectionWiseAttendance(Long sectionId, Long courseId) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        return attendanceRepository.findByCourseAndSection(course, section);
    }

    // Retrieve attendance by student, course, and date
    public Optional<Attendance> getAttendanceByStudentCourseAndDate(Long studentId, Long courseId, LocalDate attendanceDate) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + studentId));

        // Fetch the course entity or throw exception
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + courseId));

        // Fetch the enrollment entity or throw exception
        Enrollment enrollment = enrollmentRepository.findByStudentIdAndCourseId(student.getId(), course.getId())
                .orElseThrow(() -> new EntityNotFoundException("Enrollment not found for Student ID: " + studentId + " and Course ID: " + courseId));

        // Fetch and return attendance records
        return attendanceRepository.findByEnrollmentAndAttendanceDate(enrollment,attendanceDate);

    }

    // Method for bulk upload of attendance for a section using enrollment
//    @Transactional
//    public String uploadBulkAttendance(Long sectionId, Long courseId, List<AttendanceData> attendanceDataList, LocalDate attendanceDate) {
//        Section section = sectionRepository.findById(sectionId)
//                .orElseThrow(() -> new RuntimeException("Section not found"));
//
//        Course course = courseRepository.findById(courseId)
//                .orElseThrow(() -> new RuntimeException("Course not found"));
//
//        for (AttendanceData data : attendanceDataList) {
//            logger.info("Setting attendance for student with Id: {}  and getPresentStatus: {}",data.getStudentId(),data.getPresentStatus());
//            Optional<Enrollment> enrollmentOpt = enrollmentRepository.findByStudentIdAndCourseId(data.getStudentId(), course.getId());
//            if (enrollmentOpt.getStatus()) {
//                Enrollment enrollment = enrollmentOpt.get();
//
//                // Check if attendance for this enrollment already exists on the same day
//                Optional<Attendance> existingAttendance = attendanceRepository.findByStudentAndCourseAndAttendanceDate(enrollment.getStudent(), enrollment.getCourse(), attendanceDate);
//
//                if (existingAttendance.getStatus()) {
//                    // Update the existing attendance record
//                    Attendance attendance = existingAttendance.get();
//                    attendance.setStatus(data.getPresentStatus());
//                    attendanceRepository.save(attendance);
//                } else {
//                    // Create new attendance record
//                    Attendance attendance = new Attendance(enrollment, attendanceDate, data.getPresentStatus());
//                    attendanceRepository.save(attendance);
//                }
//            } else {
//                throw new RuntimeException("Enrollment not found for student ID: " + data.getStudentId() + " and course ID: " + courseId);
//            }
//        }
//
//        return "Bulk attendance uploaded successfully for section " + section.getSectionName();
//    }
    @Transactional
    public String uploadBulkAttendance(Long sectionId, Long courseId, List<AttendanceData> attendanceDataList, LocalDate attendanceDate) {
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        for (AttendanceData data : attendanceDataList) {
            logger.info("Setting attendance for student with Id: {} and getStatus: {}", data.getStudentId(), data.getStatus());
            Optional<Enrollment> enrollmentOpt = enrollmentRepository.findByStudentIdAndCourseId(data.getStudentId(), course.getId());
            if (enrollmentOpt.isPresent()) {
                Enrollment enrollment = enrollmentOpt.get();

                // Check if attendance for this enrollment already exists on the same day
                Optional<Attendance> existingAttendance = attendanceRepository.findByStudentAndCourseAndAttendanceDate(
                        enrollment.getStudent(),
                        enrollment.getCourse(),
                        attendanceDate
                );

                if (existingAttendance.isPresent()) {
                    // Update the existing attendance record
                    Attendance attendance = existingAttendance.get();
                    attendance.setPresent(data.getStatus());
                    attendanceRepository.save(attendance);
                } else {
                    // Create new attendance record
                    Attendance attendance = new Attendance(enrollment, attendanceDate, data.getStatus());
                    attendanceRepository.save(attendance);
                }
            } else {
                throw new RuntimeException("Enrollment not found for student ID: " + data.getStudentId() + " and course ID: " + courseId);
            }
        }

        return "Bulk attendance uploaded successfully for section " + section.getSectionName();
    }


}