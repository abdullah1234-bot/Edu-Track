package com.fifth_semester.project.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "teachers")
public class Teacher extends User {

    @Column(unique = true)
    private String teacherId;
    private String department;
    private String officeHours;
    private LocalDate dateOfHire;
    private String qualification;
    private String specialization;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Course> courses;

    public Teacher() {}

    public Teacher(String username, String email, String password, String teacherId, String department, String officeHours, LocalDate dateOfHire, String qualification, String specialization) {
        super(username, email, password);
        this.teacherId = teacherId;
        this.department = department;
        this.officeHours = officeHours;
        this.dateOfHire = dateOfHire;
        this.qualification = qualification;
        this.specialization = specialization;
    }

    // Getters and setters

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getOfficeHours() {
        return officeHours;
    }

    public void setOfficeHours(String officeHours) {
        this.officeHours = officeHours;
    }

    public LocalDate getDateOfHire() {
        return dateOfHire;
    }

    public void setDateOfHire(LocalDate dateOfHire) {
        this.dateOfHire = dateOfHire;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
