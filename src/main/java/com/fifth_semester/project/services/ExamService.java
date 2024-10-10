package com.fifth_semester.project.services;

import com.fifth_semester.project.entities.Exam;
import com.fifth_semester.project.entities.ExamType;
import com.fifth_semester.project.entities.Student;
import com.fifth_semester.project.repositories.CourseRepository;
import com.fifth_semester.project.repositories.ExamRepository;
import com.fifth_semester.project.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    // Retrieve all upcoming exams for a student
    public List<Exam> getUpcomingExamsForStudent(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Find all exams for the courses the student is enrolled in, and where the exam date is in the future
        return examRepository.findUpcomingExamsForStudentCourses(new ArrayList<>(student.getCoursesEnrolled()), LocalDate.now());
    }

    // Retrieve all exams for a specific course
    public List<Exam> getExamsForCourse(Long courseId) {
        return examRepository.findByCourseId(courseId);
    }

    // Schedule a new exam for a course
    public String scheduleExam(String examId, ExamType examType, LocalDate examDate, String examLocation, int duration, Long courseId) {
        Exam exam = new Exam(examId, examType, examDate, examLocation, duration, courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Course not found")));
        examRepository.save(exam);
        return "Exam scheduled successfully!";
    }
}
