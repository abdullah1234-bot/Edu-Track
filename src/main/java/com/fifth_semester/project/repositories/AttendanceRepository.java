package com.fifth_semester.project.repositories;

import com.fifth_semester.project.dtos.response.CourseAndSectionAttendanceDTO;
import com.fifth_semester.project.dtos.response.StudentAttendanceResponseDTO;
import com.fifth_semester.project.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

//@Repository
//public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
////    List<Attendance> findByStudentId(Long studentId);
////    List<Attendance> findByCourseId(Long courseId);
//    // Get attendance for a specific student and course
//    List<Attendance> findByStudentAndCourse(Student student, Course course);
//    // Get attendance for all students in a course
//    List<Attendance> findByCourse(Course course);
//    // Find attendance by course and section (student's section)
//    List<Attendance> findByEnrollmentCourseAndEnrollmentSection(Course course, Section section);
//    // Find attendance by student, course, and date
//    Optional<Attendance> findByStudentAndCourseAndAttendanceDate(Student student, Course course, LocalDate attendanceDate);
//    // Find attendance records by section and course
//    List<Attendance> findBySectionAndCourse(Section section, Course course);
//    List<Attendance> findByStudent(Student student);
//
//}

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    @Query("SELECT a FROM Attendance a WHERE a.enrollment.student.id = :studentId")
    List<Attendance> findByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT new com.fifth_semester.project.dtos.response.StudentAttendanceResponseDTO(" +
            "a.attendanceDate, a.isPresent, c.courseName, s.sectionName) " +
            "FROM Attendance a " +
            "JOIN a.enrollment e " +
            "JOIN e.course c " +
            "JOIN e.section s " +
            "WHERE e.student.id = :studentId")
    List<StudentAttendanceResponseDTO> findByStudentIdwithEnrollmentDetails(@Param("studentId") Long studentId);

    @Query("SELECT a FROM Attendance a WHERE a.enrollment.course.id = :courseId")
    List<Attendance> findByCourseId(@Param("courseId") Long courseId);

    @Query("SELECT a FROM Attendance a WHERE a.enrollment.student = :student AND a.enrollment.course = :course")
    List<Attendance> findByStudentAndCourse(@Param("student") Student student, @Param("course") Course course);


    @Query("SELECT new com.fifth_semester.project.dtos.response.CourseAndSectionAttendanceDTO(" +
            "a.attendanceDate, a.isPresent, c.courseName, s.username, se.sectionName) " +
            "FROM Attendance a " +
            "JOIN a.enrollment e " +
            "JOIN e.course c " +
            "JOIN e.student s " +
            "JOIN e.section se " +
            "WHERE c = :course AND se = :section"
    )
    List<CourseAndSectionAttendanceDTO> findByCourseAndSection(@Param("course") Course course, @Param("section") Section section);

    @Query("SELECT a FROM Attendance a WHERE a.enrollment.course = :course")
    List<Attendance> findByCourse(@Param("course") Course course);

    @Query("SELECT a FROM Attendance a WHERE a.enrollment.student = :student AND a.enrollment.course = :course AND a.attendanceDate = :attendanceDate")
    Optional<Attendance> findByStudentAndCourseAndAttendanceDate(@Param("student") Student student, @Param("course") Course course, @Param("attendanceDate") LocalDate attendanceDate);

    @Query("SELECT a FROM Attendance a WHERE a.enrollment.section = :section AND a.enrollment.course = :course")
    List<Attendance> findBySectionAndCourse(@Param("section") Section section, @Param("course") Course course);

    @Query("SELECT a FROM Attendance a WHERE a.enrollment.student = :student")
    List<Attendance> findByStudent(@Param("student") Student student);

    List<Attendance> findByEnrollmentCourseCourseName(String courseName);

    List<Attendance> findByEnrollment(Enrollment enrollment);

    Optional<Attendance> findByEnrollmentAndAttendanceDate(Enrollment enrollment, LocalDate attendanceDate);
}
