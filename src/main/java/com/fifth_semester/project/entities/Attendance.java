package com.fifth_semester.project.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "attendances")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "enrollment_id", nullable = false)
    @JsonBackReference
    private Enrollment enrollment;

    private LocalDate attendanceDate;

    @Column(nullable = false)
    private boolean isPresent;

    public Attendance() {}

    public Attendance(Enrollment enrollment, LocalDate attendanceDate, boolean isPresent) {
        this.enrollment = enrollment;
        this.attendanceDate = attendanceDate;
        this.isPresent = isPresent;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }
}
