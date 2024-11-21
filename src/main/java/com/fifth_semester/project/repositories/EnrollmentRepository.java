package com.fifth_semester.project.repositories;

import com.fifth_semester.project.entities.Course;
import com.fifth_semester.project.entities.Enrollment;
import com.fifth_semester.project.entities.Section;
import com.fifth_semester.project.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    // Check if a student is already enrolled in a course
    boolean existsByStudentAndCourse(Student student, Course course);

    @Query("SELECT e FROM Enrollment e WHERE e.student.id = :studentId AND e.course.id = :courseId")
    Optional<Enrollment> findByStudentIdAndCourseId(Long studentId, Long courseId);

    // Fetch enrollments for a student in the current semester
    List<Enrollment> findByStudentAndSemester(Student student, Integer semester);
    List<Enrollment> findByStudent(Student student);
    Optional<Enrollment> findByStudentAndCourse(Student student, Course course);
    List<Enrollment> findByCourse(Course course);
    List<Student> findStudentsByCourseId(Long courseId);
    @Query("SELECT e.student FROM Enrollment e WHERE e.course.id = :courseId AND e.section.id = :sectionId")
    List<Student> findStudentsByCourseIdAndSectionId(@Param("courseId") Long courseId, @Param("sectionId") Long sectionId);

    Optional<Enrollment> findByStudentAndCourseAndSection(Student student, Course course, Section section);

    Optional<Enrollment> findByStudentStudentIdAndCourseCourseNameAndSectionSectionName(String studentID, String courseName, String sectionName);

    @Query(value = "SELECT e.* FROM enrollments e " +
            "JOIN students s ON e.student_id = s.id " +
            "JOIN courses c ON e.course_id = c.id " +
            "WHERE LOWER(s.id) = LOWER(:studentId) " +
            "AND LOWER(c.course_name) = LOWER(:courseName)",
            nativeQuery = true)
    Optional<Enrollment> findByStudentStudentIdAndCourseCourseName(
            @Param("studentId") String studentId,
            @Param("courseName") String courseName
    );

    Optional<Enrollment> findByStudentStudentIdAndCourseIdAndSectionSectionName(String studentID, Long courseId, String section);

    List<Enrollment> findBySectionId(Long id);
}
