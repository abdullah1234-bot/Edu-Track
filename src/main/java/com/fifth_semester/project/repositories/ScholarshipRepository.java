package com.fifth_semester.project.repositories;

import com.fifth_semester.project.entities.Scholarship;
import com.fifth_semester.project.entities.ScholarshipStatus;
import com.fifth_semester.project.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScholarshipRepository extends JpaRepository<Scholarship, Long> {
    List<Scholarship> findByStudentId(Long studentId);
    List<Scholarship> findByStudent(Student student);
    List<Scholarship> findByStatus(ScholarshipStatus status);

    Scholarship findByNameAndStudent(String scholarshipName, Student student);
}