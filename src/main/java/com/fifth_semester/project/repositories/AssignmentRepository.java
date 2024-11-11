package com.fifth_semester.project.repositories;

import com.fifth_semester.project.dtos.response.AssignmentDTO;
import com.fifth_semester.project.entities.Assignment;
import com.fifth_semester.project.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
//    List<Assignment> findByCourseId(Long courseId);
//    List<Assignment> findByStudentId(Long studentId);
    @Query(value = "SELECT a.id AS assignment_id, a.assignment_title, a.description, a.upload_date, a.due_date, a.submitted, a.graded, a.attachment, a.feedback, a.marks, s.student_id, u.username, s.emergency_contact  " +
            "FROM assignments a JOIN students s ON a.student_id = s.id JOIN users u ON s.id = u.id WHERE a.course_id = :courseId", nativeQuery = true)
    List<Object[]> findByCourse(@Param("courseId") Long courseId);
//    List<Assignment> findByStudentAndCourse(Student student, Course course);
//    List<Assignment> findByEnrollmentCourseAndEnrollmentSection(Course course, Section section);
//    List<Assignment> findByStudentAndSection(Student student, Section section);

    @Query(value = "SELECT a.id AS assignment_id, a.assignment_title, a.description, a.upload_date, a.due_date, a.submitted, a.graded, a.attachment, a.feedback, a.marks, s.student_id, u.username, s.emergency_contact  " +
            "FROM assignments a JOIN students s ON a.student_id = s.id JOIN users u ON s.id = u.id JOIN sections se ON se.id=a.section_id WHERE a.section_id = :sectionId AND a.due_date > SYSDATE()", nativeQuery = true)
    List<Object[]> findBySectionAndDueDate(@Param("sectionId") Long sectionId);

    @Query(value = "SELECT a.id AS assignment_id, a.assignment_title, a.description, a.upload_date, a.due_date, a.submitted, a.graded, a.attachment, a.feedback, a.marks, s.student_id, u.username, s.emergency_contact  " +
            "FROM assignments a " +
            "JOIN students s ON a.student_id = s.id " +
            "JOIN users u ON s.id = u.id " +
            "WHERE a.course_id = :courseId AND a.student_id = :studentId",
            nativeQuery = true)
    List<Object[]> findByCourseAndStudent(@Param("courseId") Long courseId, @Param("studentId") Long studentId);
    List<Assignment> findByDueDate(LocalDate dueDate);
    List<Assignment> findByStudent(Student student);
    @Query("SELECT a FROM Assignment a WHERE a.student = :student AND a.marks IS NOT NULL")
    List<Assignment> findAssignmentsWithNonNullMarks(@Param("student") Student student);

    @Query(value = "SELECT a.id AS assignment_id, a.assignment_title, a.description, a.upload_date, a.due_date, a.submitted, a.graded, a.attachment, a.feedback, a.marks, s.student_id, u.username, s.emergency_contact " +
            "FROM assignments a " +
            "JOIN students s ON a.student_id = s.id " +
            "JOIN sections se ON a.section_id = se.id " +
            "JOIN courses c ON se.course_id = c.id " +
            "JOIN users u ON s.id = u.id " +
            "WHERE se.course_id = :courseId AND a.student_id = :studentId",
            nativeQuery = true)
    List<Object[]> findBySectionCourseAndStudent(@Param("courseId") Long courseId, @Param("studentId") Long studentId);


}
