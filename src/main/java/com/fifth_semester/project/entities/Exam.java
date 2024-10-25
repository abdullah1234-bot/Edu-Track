package com.fifth_semester.project.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "exams",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"exam_type", "course_id"})
})
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Optional: If you need a custom identifier for the exam
    @Column(unique = true)
    private String examId;

    @Enumerated(EnumType.STRING)
    private ExamType examType;  // MIDTERM, FINAL, etc.

    private LocalDate examDate;

    private String examLocation;

    private int duration;  // Duration in minutes

    // Many Exams belong to one Course
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @JsonBackReference
    private Course course;

    public Exam() {}

    public Exam(String examId, ExamType examType, LocalDate examDate, String examLocation, int duration, Course course) {
        this.examId = examId;
        this.examType = examType;
        this.examDate = examDate;
        this.examLocation = examLocation;
        this.duration = duration;
        this.course = course;
    }

    // Getters and Setters

    // ID
    public Long getId() {
        return id;
    }

    // Exam ID
    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    // Exam Type
    public ExamType getExamType() {
        return examType;
    }

    public void setExamType(ExamType examType) {
        this.examType = examType;
    }

    // Exam Date
    public LocalDate getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }

    // Exam Location
    public String getExamLocation() {
        return examLocation;
    }

    public void setExamLocation(String examLocation) {
        this.examLocation = examLocation;
    }

    // Duration
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    // Course
    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

}
