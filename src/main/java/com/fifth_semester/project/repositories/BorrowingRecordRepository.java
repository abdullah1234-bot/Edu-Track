package com.fifth_semester.project.repositories;

import com.fifth_semester.project.entities.BorrowingRecord;
import com.fifth_semester.project.entities.Student;
import com.fifth_semester.project.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {

    // Find borrowing record by student, book, and return date is null (book is not returned yet)
    Optional<BorrowingRecord> findByStudentAndBookAndReturnDateIsNull(Student student, Book book);

    // Find overdue borrowing records for a student
//    List<BorrowingRecord> findOverdueRecordsForStudent(Student student, LocalDate currentDate);
    // Find borrowing records with return date between two dates
    List<BorrowingRecord> findByReturnDateBetween(LocalDate startDate, LocalDate endDate);

    // Find overdue records for a student where the return date is before today and not yet returned
    List<BorrowingRecord> findByStudentAndReturnDateBeforeAndReturnDateIsNull(Student student, LocalDate currentDate);

    List<BorrowingRecord> findByStudentAndBorrowDateBeforeAndReturnDateIsNull(Student student, LocalDate oneWeekAgo);
}
