package com.fifth_semester.project.repositories;

import com.fifth_semester.project.entities.Course;
import com.fifth_semester.project.entities.Grade;
import com.fifth_semester.project.entities.Section;
import com.fifth_semester.project.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByStudentId(Long studentId);
    List<Grade> findByCourseId(Long courseId);
    // Find grades by student
    List<Grade> findByStudent(Student student);
    // Find grades by course
    List<Grade> findByCourse(Course course);
    // Find a grade by student and course
    Optional<Grade> findByStudentAndCourse(Student student, Course course);
    // Find grades by course and section
    List<Grade> findByCourseAndCourseSection(Course course, Section section);

}
