package com.fifth_semester.project.repositories;

import com.fifth_semester.project.entities.Course;
import com.fifth_semester.project.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCourseId(String courseId);
//    List<Course> findByInstructorId(Long instructorId);

    // Fetch courses that the student is eligible for, based on their current semester
    @Query("SELECT c FROM Course c WHERE c.eligibleSemester <= :academicYear")
    List<Course> findCoursesForStudent(@Param("academicYear") Integer academicYear);
//
//    @Query("SELECT e.student FROM Enrollment e WHERE e.course.id = :courseId")
//    List<Student> findStudentsByCourseId(@Param("courseId") Long courseId);

    Optional<Course> findByCourseName(String courseName);
    Optional<Course> findByCourseCodeAndSemester(String courseCode, int semesterNumber);
    List<Course> findByTeacherId(Long teacherId);

    Optional<Course> findByCourseCode(String courseCode);

}
