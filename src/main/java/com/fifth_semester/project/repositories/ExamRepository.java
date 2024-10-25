package com.fifth_semester.project.repositories;

import com.fifth_semester.project.dtos.response.StudentExamsDTO;
import com.fifth_semester.project.entities.Course;
import com.fifth_semester.project.entities.Exam;
import com.fifth_semester.project.entities.ExamType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByCourseId(Long courseId);

    // Find upcoming exams for a list of courses that a student is enrolled in
    @Query("SELECT new com.fifth_semester.project.dtos.response.StudentExamsDTO(" +
            "e.examId, e.examDate, e.examLocation, e.duration, e.examType, e.course.id, c.courseName)" +
            "FROM Exam e JOIN e.course c WHERE e.course IN :courses AND e.examDate >= :currentDate ORDER BY e.examDate ASC")
    List<StudentExamsDTO> findUpcomingExamsForStudentCourses(List<Course> courses, LocalDate currentDate);

    Optional<Exam> findByCourseIdAndExamType(Long courseId, ExamType examType);

    Optional<Exam> findByExamId(String examId);
}
