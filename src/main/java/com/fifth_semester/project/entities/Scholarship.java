package com.fifth_semester.project.entities;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "scholarships")
public class Scholarship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    @JsonBackReference
    private Student student;

    @Column(nullable = false)
    private String name;  // Scholarship name

    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    private ScholarshipStatus status;  // Approved, Pending, Rejected

    private LocalDate applicationDate;
    private LocalDate decisionDate;

    public Scholarship() {}

    public Scholarship(Student student, String name, Double amount, ScholarshipStatus status, LocalDate applicationDate, LocalDate decisionDate) {
        this.student = student;
        this.name = name;
        this.amount = amount;
        this.status = status;
        this.applicationDate = applicationDate;
        this.decisionDate = decisionDate;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public ScholarshipStatus getStatus() {
        return status;
    }

    public void setStatus(ScholarshipStatus status) {
        this.status = status;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public LocalDate getDecisionDate() {
        return decisionDate;
    }

    public void setDecisionDate(LocalDate decisionDate) {
        this.decisionDate = decisionDate;
    }
}
