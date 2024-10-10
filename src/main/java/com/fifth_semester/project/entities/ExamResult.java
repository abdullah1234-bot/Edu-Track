package com.fifth_semester.project.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "exam_results")
public class ExamResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    private String score;
    private String transcriptLink;

    public ExamResult() {}

    public ExamResult(Student student, Exam exam, String score, String transcriptLink) {
        this.student = student;
        this.exam = exam;
        this.score = score;
        this.transcriptLink = transcriptLink;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTranscriptLink() {
        return transcriptLink;
    }

    public void setTranscriptLink(String transcriptLink) {
        this.transcriptLink = transcriptLink;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}

