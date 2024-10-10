package com.fifth_semester.project.repositories;

import com.fifth_semester.project.entities.Fee;
import com.fifth_semester.project.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeeRepository extends JpaRepository<Fee, Long> {
    Optional<Fee> findByStudentId(Long studentId);
    List<Fee> findByStudent(Student student);
    Optional<Fee> findByStudentAndPeriod(Student student, String period);
    List<Fee> findByStudentAndStatusIn(Student student, List<String> unpaid);
    // Find the latest unpaid fee for a student
    Optional<Fee> findTopByStudentAndStatusInOrderByPeriodDesc(Student student, List<String> statuses);
    // Find the latest unpaid fee record for a student
//    Optional<Fee> findLatestUnpaidFeeForStudent(Student student);
    @Query(value = "SELECT * FROM fees f WHERE f.student_id = :studentId AND f.status = 'Unpaid' ORDER BY f.period DESC LIMIT 1", nativeQuery = true)
    Optional<Fee> findLatestUnpaidFeeForStudent(@Param("studentId") Long studentId);

    @Query("SELECT f FROM Fee f WHERE f.student.id = :studentId AND f.status = 'Unpaid' ORDER BY f.period DESC")
    Optional<List<Fee>> findUnpaidFeesByStudent(@Param("studentId") Long studentId);
}
