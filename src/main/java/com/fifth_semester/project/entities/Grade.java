package com.fifth_semester.project.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "grades")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Enumerated(EnumType.STRING)
    private GradeValue value;  // Grade value (A, B, C, D, E, F)

    private int marks;  // Marks out of 100

    private String feedback;  // Optional feedback from the teacher

    public Grade() {}

    public Grade(Student student, Course course, int marks, String feedback) {
        this.student = student;
        this.course = course;
        this.marks = marks;
        this.value = calculateGradeValue(marks);
        this.feedback = feedback;
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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public GradeValue getValue() {
        return value;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
        this.value = calculateGradeValue(marks);
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

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
}
