package com.fifth_semester.project.repositories;

import com.fifth_semester.project.dtos.response.GradeDTO;
import com.fifth_semester.project.dtos.response.StudentExamResultDTO;
import com.fifth_semester.project.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
//    List<Grade> findByStudentId(Long studentId);
//    List<Grade> findByCourseId(Long courseId);
    // Find grades by student
    List<Grade> findByEnrollmentStudent(Student student);
    // Find grades by course
//    List<Grade> findByEnrollmentCourse(Course course);
    // Find a grade by student and course
//    Optional<Grade> findByStudentAndCourse(Student student, Course course);
    // Find grades by course and section
    @Query("SELECT new com.fifth_semester.project.dtos.response.GradeDTO(" +
            "g.id, " +
            "e.student.studentId, " +
            "ss.username, " +
            "c.id, " +
            "c.courseName, " +
            "s.sectionName, " +
            "g.examType, " +
            "g.marks, " +
            "g.feedback, " +
            "g.value) " +
            "FROM Grade g " +
            "JOIN g.enrollment e " +
            "JOIN e.student ss "+
            "JOIN e.course c " +
            "JOIN e.section s " +
            "WHERE c = :course AND s = :section")
    List<GradeDTO> findByEnrollmentCourseAndEnrollmentSection(@Param("course") Course course,
                                                              @Param("section") Section section);

    Optional<Grade> findByEnrollmentAndEnrollmentCourse(Enrollment enrollment, Course course);

    Optional<Grade> findByEnrollmentAndEnrollmentCourseAndEnrollmentSectionAndExamType(Enrollment enrollment, Course course, Section section, ExamType examType);

    Optional<Grade> findByEnrollmentAndEnrollmentCourseAndEnrollmentSection(Enrollment enrollment, Course course, Section section);

    @Query("SELECT new com.fifth_semester.project.dtos.response.GradeDTO(" +
            "g.id, " +
            "e.student.studentId, " +
            "ss.username, " +
            "c.id, " +
            "c.courseName, " +
            "s.sectionName, " +
            "g.examType, " +
            "g.marks, " +
            "g.feedback, " +
            "g.value) " +
            "FROM Grade g " +
            "JOIN g.enrollment e " +
            "JOIN e.student ss "+
            "JOIN e.course c " +
            "JOIN e.section s " +
            "WHERE c = :course")
    List<GradeDTO> findByEnrollmentCourse(@Param("course") Course course);

    @Query("SELECT new com.fifth_semester.project.dtos.response.GradeDTO(" +
            "g.id, " +
            "e.student.studentId, " +
            "ss.username, " +
            "c.id, " +
            "c.courseName, " +
            "s.sectionName, " +
            "g.examType, " +
            "g.marks, " +
            "g.feedback, " +
            "g.value) " +
            "FROM Grade g " +
            "JOIN g.enrollment e " +
            "JOIN e.student ss "+
            "JOIN e.course c " +
            "JOIN e.section s " +
            "WHERE ss = :student")
    List<GradeDTO> findByEnrollmentOfStudent(@Param("student") Student student);
    @Query(
            "SELECT new com.fifth_semester.project.dtos.response.StudentExamResultDTO(" +
                    "c.id," +
                    "c.courseName," +
                    "g.examType," +
                    "g.marks," +
                    "g.feedback)" +
                    "FROM Grade g " +
                    "JOIN g.enrollment e " +
                    "JOIN e.course c " +
                    "WHERE e = :enrollment"
    )
    List<StudentExamResultDTO> findStudentExamResultByEnrollment(Enrollment enrollment);

    @Query("SELECT SUM(g.marks) FROM Grade g WHERE g.enrollment.student = :student AND g.enrollment.course = :course AND g.examType = 'QUIZ'")
    Integer findTotalQuizMarks(@Param("student") Student student, @Param("course") Course course);
}
