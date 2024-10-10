package com.fifth_semester.project.repositories;

import com.fifth_semester.project.entities.Course;
import com.fifth_semester.project.entities.Exam;
import com.fifth_semester.project.entities.ExamType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByCourseId(Long courseId);

    // Find upcoming exams for a list of courses that a student is enrolled in
    @Query("SELECT e FROM Exam e WHERE e.course IN :courses AND e.examDate >= :currentDate ORDER BY e.examDate ASC")
    List<Exam> findUpcomingExamsForStudentCourses(List<Course> courses, LocalDate currentDate);

    // Optional: Find exams by exam type, if needed
    List<Exam> findByCourseIdAndExamType(Long courseId, ExamType examType);
}
