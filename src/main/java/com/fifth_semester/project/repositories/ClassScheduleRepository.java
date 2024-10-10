package com.fifth_semester.project.repositories;

import com.fifth_semester.project.entities.ClassSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClassScheduleRepository extends JpaRepository<ClassSchedule, Long> {

    // Fetch schedules for a teacher
    List<ClassSchedule> findByTeacherId(Long teacherId);

    // Fetch schedules for a course
    List<ClassSchedule> findByCourseId(Long courseId);

    // Query class schedules based on field of study, semester, and section
//    List<ClassSchedule> findByFieldOfStudyAndSemesterAndSection(String fieldOfStudy, Integer semester, String section);

    // Fetch schedules for a student by course IDs and date range
    List<ClassSchedule> findByCourseIdInAndClassDateBetween(List<Long> courseIds, LocalDate startDate, LocalDate endDate);
}
