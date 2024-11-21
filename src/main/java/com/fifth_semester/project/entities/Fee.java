package com.fifth_semester.project.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "fees", uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "period"}))
public class Fee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    @JsonBackReference
    private Student student;  // Many fees for one student

    @Column(nullable = false)
    private Double totalAmount;

    private Double paidAmount;
    private Double dueAmount;

    @Column(nullable = false)
    private String status;  // Paid, Unpaid, Overdue, etc.

    @Column(nullable = false)
    private String period;  // e.g., "Fall 2024", "Spring 2024" for tracking

    public Fee() {}

    public Fee(Student student, Double totalAmount, Double paidAmount, Double dueAmount, String status, String period) {
        this.student = student;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.dueAmount = dueAmount;
        this.status = status;
        this.period = period;
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

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Double paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Double getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(Double dueAmount) {
        this.dueAmount = dueAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
