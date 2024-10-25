package com.fifth_semester.project.services;

import com.fifth_semester.project.dtos.request.UpdateExamRequest;
import com.fifth_semester.project.dtos.response.StudentExamsDTO;
import com.fifth_semester.project.entities.Course;
import com.fifth_semester.project.entities.Exam;
import com.fifth_semester.project.entities.ExamType;
import com.fifth_semester.project.entities.Student;
import com.fifth_semester.project.repositories.CourseRepository;
import com.fifth_semester.project.repositories.ExamRepository;
import com.fifth_semester.project.repositories.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<StudentExamsDTO> getUpcomingExamsForStudent(Long studentId) {
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

    @Transactional
    public String updateExam(Long examId, UpdateExamRequest updateExamRequest) {
        // Fetch the exam by examId
        Exam exam = examRepository.findByExamId(String.valueOf(examId))
                .orElseThrow(() -> new EntityNotFoundException("Exam not found with ID: " + examId));

        // Update fields if present in the request
        if (updateExamRequest.getExamType() != null) {
            exam.setExamType(updateExamRequest.getExamType());
        }

        if (updateExamRequest.getExamDate() != null) {
            exam.setExamDate(updateExamRequest.getExamDate());
        }

        if (updateExamRequest.getExamLocation() != null && !updateExamRequest.getExamLocation().isEmpty()) {
            exam.setExamLocation(updateExamRequest.getExamLocation());
        }

        if (updateExamRequest.getDuration() != null) {
            exam.setDuration(updateExamRequest.getDuration());
        }

        if (updateExamRequest.getCourseId() != null) {
            Course course = courseRepository.findById(updateExamRequest.getCourseId())
                    .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + updateExamRequest.getCourseId()));
            exam.setCourse(course);
        }

        // Save the updated exam
        examRepository.save(exam);

        return "Exam updated successfully!";
    }
}
