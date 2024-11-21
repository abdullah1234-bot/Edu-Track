package com.fifth_semester.project.repositories;

import com.fifth_semester.project.dtos.response.InfoTeacherDTO;
import com.fifth_semester.project.entities.Librarian;
import com.fifth_semester.project.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByTeacherId(String teacherId);
    Optional<Teacher> findByEmail(String email);
    Optional<Teacher> findByUsername(String username);
    @Query("SELECT t FROM Teacher t WHERE t.username = :username OR t.email = :email")
    Optional<Teacher> findByUsernameOrEmail(String username, String email);


//    @Query("SELECT new com.fifth_semester.project.dtos.response.InfoTeacherDTO(" +
//            "t.id, " +
//            "t.username, " +
//            "t.email, " +
//            "t.teacherId, " +
//            "t.department, " +
//            "t.officeHours, " +
//            "t.dateOfHire, " +
//            "t.qualification, " +
//            "t.specialization, " +
//            "t.sections, " +
//            "(SELECT new com.fifth_semester.project.dtos.response.CourseDTO(" +
//            "c.id, " +
//            "c.courseName, " +
//            "c.courseCode, " +
//            "c.creditHours, " +
//            "c.description" +
//            ") FROM Course c WHERE c IN (SELECT DISTINCT s.course FROM Section s WHERE s.teacher = t)), " +
//            "(SELECT new com.fifth_semester.project.dtos.response.ClassScheduleDTO(" +
//            "cs.id, " +
//            "s.course.id, " +
//            "s.course.courseName, " +
//            "s.course.courseCode, " +
//            "s.sectionName, " +
//            "t.id, " +
//            "t.username, " +
//            "cs.startTime, " +
//            "cs.endTime, " +
//            "cs.classroom, " +
//            "cs.day" +
//            ") FROM ClassSchedule cs " +
//            "JOIN cs.section s " +
//            "WHERE s.teacher = t)" +
//            ") " +
//            "FROM Teacher t " +
//            "WHERE t = :teacher")
//    InfoTeacherDTO getTeacherInfo(@Param("teacher") Teacher teacher);


    @Query("SELECT new com.fifth_semester.project.dtos.response.InfoTeacherDTO(" +
            "t.id, " +
            "t.username, " +
            "t.email, " +
            "t.teacherId, " +
            "t.department, " +
            "t.officeHours, " +
            "t.dateOfHire, " +
            "t.qualification, " +
            "t.specialization " +
            ") " +
            "FROM Teacher t " +
            "WHERE t = :teacher")
    InfoTeacherDTO getTeacherInfo(@Param("teacher") Teacher teacher);

}