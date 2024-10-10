package com.fifth_semester.project.entities;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "exams")
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

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
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

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public ExamType getExamType() {
        return examType;
    }

    public void setExamType(ExamType examType) {
        this.examType = examType;
    }

    public LocalDate getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }

    public String getExamLocation() {
        return examLocation;
    }

    public void setExamLocation(String examLocation) {
        this.examLocation = examLocation;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
