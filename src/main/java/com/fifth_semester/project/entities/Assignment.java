package com.fifth_semester.project.entities;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "assignments")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String assignmentTitle;
    private String description;
    private LocalDate uploadDate;
    private LocalDate dueDate;
    private boolean submitted;
    private boolean graded;
    private String attachment;  // Assuming this is a file path or URL
    private String feedback;  // Feedback from the teacher
    private int marks;  // Marks for the assignment

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonBackReference
    private Student student;

    // Optional: Many Exams belong to one Section
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    @JsonBackReference
    private Section section;


    public Assignment() {}

    public Assignment(String assignmentTitle, String description, Student student, LocalDate uploadDate, LocalDate dueDate, boolean submitted, boolean graded, String attachment) {
        this.assignmentTitle = assignmentTitle;
        this.description = description;
        this.student = student;
        this.uploadDate = uploadDate;
        this.dueDate = dueDate;
        this.submitted = submitted;
        this.graded = graded;
        this.attachment = attachment;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAssignmentTitle() {
        return assignmentTitle;
    }

    public void setAssignmentTitle(String assignmentTitle) {
        this.assignmentTitle = assignmentTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public LocalDate getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDate uploadDate) {
        this.uploadDate = uploadDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    public boolean isGraded() {
        return graded;
    }

    public void setGraded(boolean graded) {
        this.graded = graded;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }
}
