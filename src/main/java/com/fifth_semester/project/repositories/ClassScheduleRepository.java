package com.fifth_semester.project.repositories;

import com.fifth_semester.project.entities.ClassSchedule;
import com.fifth_semester.project.entities.Course;
import com.fifth_semester.project.entities.Section;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClassScheduleRepository extends JpaRepository<ClassSchedule, Long> {

    // Fetch schedules for a teacher
    List<ClassSchedule> findByTeacherId(Long teacherId);
    List<ClassSchedule> findByDayAndCourseIdInAndSectionIdIn(String day, List<Long> courseIds, List<Long> sectionIds);
    // Fetch schedules for a course
    List<ClassSchedule> findByCourseId(Long courseId);
    Optional<ClassSchedule> findBySectionAndDayAndStartTimeAndEndTime(
            Section section, String day, LocalTime startTime, LocalTime endTime);
    // Query class schedules based on field of study, semester, and section
//    List<ClassSchedule> findByFieldOfStudyAndSemesterAndSection(String fieldOfStudy, Integer semester, String section);
    @Query("SELECT c FROM ClassSchedule c WHERE c.course.id IN :courseIds AND c.day = :day")
    List<ClassSchedule> findByCourseIdInAndDay(@Param("courseIds") List<Long> courseIds, @Param("day") String day);
    // Fetch schedules for a student by course IDs and date range
//    List<ClassSchedule> findByCourseIdInAndClassDateBetween(List<Long> courseIds, Time startDate, Time endDate);

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE class_schedules", nativeQuery = true)
    void truncateMyEntityTable();

    List<ClassSchedule> findByDayAndCourseIdIn(String day, List<Long> courseIds);

    List<ClassSchedule> findByDayAndSectionIdIn(String day, List<Long> sectionIds);

    List<ClassSchedule> findByDayAndCourseIdAndSectionId(String day, Long courseId, Long sectionId);

    Optional<ClassSchedule> findByDayAndCourseIdAndSectionIdAndStartTimeAndEndTime(String upperCase, Long id, Long id1, LocalTime startTime, LocalTime endTime);
    @Query("SELECT cs FROM ClassSchedule cs " +
            "JOIN cs.course c " +
            "JOIN cs.section s " +
            "WHERE LOWER(cs.day) = LOWER(:day) " +
            "AND LOWER(c.courseName) = LOWER(:courseName) " +
            "AND LOWER(s.sectionName) = LOWER(:sectionName) " +
            "AND cs.startTime = :startTime " +
            "AND cs.endTime = :endTime")
    Optional<ClassSchedule> findByDayAndCourseCourseNameAndSectionSectionNameAndStartTimeAndEndTime(
            @Param("day") String day,
            @Param("courseName") String courseName,
            @Param("sectionName") String sectionName,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}
