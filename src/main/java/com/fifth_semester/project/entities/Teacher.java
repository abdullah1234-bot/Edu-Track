package com.fifth_semester.project.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teachers")
public class Teacher extends User {

    @Column(unique = true, nullable = false)
    private String teacherId;
    private String department;
    private String officeHours;
    private LocalDate dateOfHire;
    private String qualification;
    private String specialization;

    // One Teacher has many Sections
    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Section> sections = new ArrayList<>();

    // One Teacher has many Courses
    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Course> courses = new ArrayList<>();

    // One Teacher has many ClassSchedules (assuming ClassSchedule entity exists)
    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ClassSchedule> classSchedules = new ArrayList<>();

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

    // Getters and Setters

    // Teacher ID
    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    // Department
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    // Office Hours
    public String getOfficeHours() {
        return officeHours;
    }

    public void setOfficeHours(String officeHours) {
        this.officeHours = officeHours;
    }

    // Date of Hire
    public LocalDate getDateOfHire() {
        return dateOfHire;
    }

    public void setDateOfHire(LocalDate dateOfHire) {
        this.dateOfHire = dateOfHire;
    }

    // Qualification
    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    // Specialization
    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    // Sections
    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    // Courses
    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    // ClassSchedules
    public List<ClassSchedule> getClassSchedules() {
        return classSchedules;
    }

    public void setClassSchedules(List<ClassSchedule> classSchedules) {
        this.classSchedules = classSchedules;
    }

    // Helper Methods
    public void addSection(Section section) {
        sections.add(section);
        section.setTeacher(this);
    }

    public void removeSection(Section section) {
        sections.remove(section);
        section.setTeacher(null);
    }

    public void addCourse(Course course) {
        courses.add(course);
        course.setTeacher(this);
    }

    public void removeCourse(Course course) {
        courses.remove(course);
        course.setTeacher(null);
    }

//    public void addClassSchedule(ClassSchedule classSchedule) {
//        classSchedules.add(classSchedule);
//        classSchedule.setTeacher(this);
//    }
//
//    public void removeClassSchedule(ClassSchedule classSchedule) {
//        classSchedules.remove(classSchedule);
//        classSchedule.setTeacher(null);
//    }
}
