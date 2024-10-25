package com.fifth_semester.project.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import javax.validation.constraints.NotNull;

@Entity
@Table(name = "grades")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many Grades belong to one Enrollment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    @JsonBackReference
    private Enrollment enrollment;

    @Enumerated(EnumType.STRING)
    private GradeValue value;  // Grade value (A, B, C, D, E, F)
    @NotNull
    @Enumerated(EnumType.STRING)
    private ExamType examType;
    private int marks;  // Marks out of 100

    private String feedback;  // Optional feedback from the teacher

    public Grade() {}

    public Grade(Enrollment enrollment, int marks, String feedback,ExamType examType) {
        this.enrollment = enrollment;
        this.marks = marks;
        this.value = calculateGradeValue(marks);
        this.feedback = feedback;
        this.examType = examType;
    }

    // Getters and Setters

    // ID
    public Long getId() {
        return id;
    }

    // Enrollment
    public Enrollment getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    // Grade Value
    public GradeValue getValue() {
        return value;
    }

    public void setValue(GradeValue value) {
        this.value = value;
    }

    // Marks
    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
        this.value = calculateGradeValue(marks);
    }

    // Feedback
    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    // Private method to calculate GradeValue based on marks
    private GradeValue calculateGradeValue(int marks) {
        if (marks >= 85) {
            return GradeValue.A;
        } else if (marks >= 70) {
            return GradeValue.B;
        } else if (marks >= 60) {
            return GradeValue.C;
        } else if (marks >= 50) {
            return GradeValue.D;
        } else if (marks >= 40) {
            return GradeValue.E;
        } else {
            return GradeValue.F;  // F for marks less than 40
        }
    }

    public ExamType getExamType() {
        return examType;
    }

    public void setExamType(ExamType examType) {
        this.examType = examType;
    }
}
