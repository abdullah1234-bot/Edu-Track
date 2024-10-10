package com.fifth_semester.project.repositories;

import com.fifth_semester.project.entities.Assignment;
import com.fifth_semester.project.entities.Course;
import com.fifth_semester.project.entities.Section;
import com.fifth_semester.project.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByCourseId(Long courseId);
    List<Assignment> findByStudentId(Long studentId);
    List<Assignment> findByCourse(Course course);
    List<Assignment> findByStudentAndCourse(Student student, Course course);
    List<Assignment> findByCourseAndSection(Course course, Section section);
    List<Assignment> findByStudentAndSection(Student student, Section section);

    @Query("SELECT a FROM Assignment a WHERE a.course = :course AND a.course.section = :section AND a.dueDate >= :currentDate")
    List<Assignment> findByCourseAndSectionAndDueDate(Course course, Section section, LocalDate currentDate);

    List<Assignment> findByDueDate(LocalDate dueDate);
    List<Assignment> findByStudent(Student student);
}
