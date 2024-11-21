package com.fifth_semester.project.repositories;

import com.fifth_semester.project.entities.Course;
import com.fifth_semester.project.entities.Section;
import com.fifth_semester.project.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {

    @Query(
            value = "SELECT s.* FROM sections s LEFT JOIN enrollments e ON s.id = e.section_id " +
                    "WHERE s.course_id = :courseId GROUP BY s.id HAVING COUNT(e.id) < 50 " +
                    "ORDER BY s.section_name ASC LIMIT 1",
            nativeQuery = true
    )
    Optional<Section> findFirstSectionByCourseWithStudentCountLessThan(@Param("courseId") Long courseId);

    Optional<Section> findTopByCourseOrderBySectionNameDesc(Course course);

    Optional<Section> findByCourseAndSectionName(Course course, String sectionName);

//    Optional<Section> findByNameAndCourse(String sectionName, Course course);

    Optional<Section> findBySectionNameAndCourse(String sectionName, Course course);

    Optional<Section> findByIdAndCourse(Long sectionId, Course course);

    Optional<Section> findBySectionName(String sectionName);

    Optional<Section> findBySectionNameAndCourseCourseNameAndTeacherUsername(String sectionName, String courseName, String teacherName);

    //    List<Section> findByCourseIdAndStudentCountLessThanOrderBySectionNameAsc(Long courseId, int studentCount);
    @Query("SELECT s FROM Section s " +
            "JOIN s.course c " +
            "JOIN s.teacher t " +
            "WHERE LOWER(s.sectionName) = LOWER(:sectionName) " +
            "AND LOWER(c.courseName) = LOWER(:courseName) " +
            "AND LOWER(t.username) = LOWER(:teacherName)")
    Optional<Section> findBySectionNameAndCourseNameAndTeacherUsername(
            @Param("sectionName") String sectionName,
            @Param("courseName") String courseName,
            @Param("teacherName") String teacherName
    );

    Optional<Section> findByCourseAndTeacher(Course course, Teacher teacher);

    @Transactional
    @Modifying
    @Query("UPDATE Section s " +
            "SET s.teacher = null " +
            "WHERE s.course = :course AND s.teacher = :teacher")
    void updateSectionOfCourseAndTeacher(@Param("course") Course course, @Param("teacher") Teacher teacher);

}
