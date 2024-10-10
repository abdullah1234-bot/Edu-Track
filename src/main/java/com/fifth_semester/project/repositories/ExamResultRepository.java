package com.fifth_semester.project.repositories;

import com.fifth_semester.project.entities.Course;
import com.fifth_semester.project.entities.ExamResult;
import com.fifth_semester.project.entities.ExamType;
import com.fifth_semester.project.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {
//    List<ExamResult> findByStudentId(Long studentId);
//    List<ExamResult> findByExamId(Long examId);
    List<ExamResult> findByStudent(Student student);
    // Find exam results by student and semester
//    List<ExamResult> findExamResultsByStudentAndSemester(Student student, int semester);
    // Find exam results by student, course, and exam type
    List<ExamResult> findExamResultsByStudentAndCourseAndExamExamType(Student student, Course course, ExamType examType);
}
