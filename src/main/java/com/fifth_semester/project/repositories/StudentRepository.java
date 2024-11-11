package com.fifth_semester.project.repositories;

import com.fifth_semester.project.dtos.response.InfoStudentDTO;
import com.fifth_semester.project.dtos.response.StudentDTO;
import com.fifth_semester.project.entities.Course;
import com.fifth_semester.project.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentId(String studentId);
    Optional<Student> findByEmail(String email);

    @Query("SELECT e.course FROM Enrollment e WHERE e.student.id = :studentId")
    List<Course> findCoursesByStudentId(@Param("studentId") Long studentId);

    @Query("SELECT new com.fifth_semester.project.dtos.response.StudentDTO(s.id, s.email, s.studentId, s.username) " +
            "FROM Student s WHERE s.parent.id = :parentId")
    List<StudentDTO> findStudentDTOsByParentId(Long parentId);

//    @Query("SELECT new com.fifth_semester.project.dtos.response.InfoStudentDTO(" +
//            "s.id, " +
//            "s.username, " +
//            "s.email, " +
//            "s.studentId, " +
//            "s.academicYear, " +
//            "s.semester, " +
//            "s.profilePicture, " +
//            "s.emergencyContact, " +
//            "s.address, " +
//            "s.dateOfBirth, " +
//            "s.enrollments, " +
//            "s.scholarships, " +
//            "s.fees, " +
//            "s.borrowingRecords, " +
//            "p.username, " +
//            "p.email, " +
//            "p.contactNumber, " +
//            "p.address, " +
//            "p.occupation, " +
//            "(SELECT new com.fifth_semester.project.dtos.response.InfoAssignment(" +
//            "a.id, " +
//            "a.assignmentTitle, " +
//            "a.description, " +
//            "a.uploadDate, " +
//            "a.dueDate, " +
//            "a.submitted, " +
//            "a.graded, " +
//            "a.attachment, " +
//            "a.feedback, " +
//            "a.marks" +
//            ") FROM Assignment a WHERE a.student = s)" +
//            ") " +
//            "FROM Student s " +
//            "LEFT JOIN s.parent p " +
//            "WHERE s = :student")
//    InfoStudentDTO getStudentInfo(@Param("student") Student student);

    @Query("SELECT new com.fifth_semester.project.dtos.response.InfoStudentDTO(" +
            "s.id, " +
            "s.username, " +
            "s.email, " +
            "s.studentId, " +
            "s.academicYear, " +
            "s.semester, " +
            "s.profilePicture, " +
            "s.emergencyContact, " +
            "s.address, " +
            "s.dateOfBirth, " +
            "p.username, " +
            "p.email, " +
            "p.contactNumber, " +
            "p.address, " +
            "p.occupation " +
            ") " +
            "FROM Student s " +
            "LEFT JOIN s.parent p " +
            "WHERE s = :student")
    InfoStudentDTO getStudentInfo(@Param("student") Student student);
}