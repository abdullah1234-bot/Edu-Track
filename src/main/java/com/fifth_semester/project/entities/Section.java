package com.fifth_semester.project.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sections")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many Sections belong to one Course
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @JsonBackReference
    private Course course;

    @Column(name = "section_name", nullable = false)
    private String sectionName;

    // Many Sections can have one Teacher
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    @JsonBackReference
    private Teacher teacher;

    // One Section has many Enrollments
    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Enrollment> enrollments = new HashSet<>();

    // One Section has many Grades
//    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
//    private Set<Grade> grades = new HashSet<>();

    // One Section has many Assignments
    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Assignment> assignments = new HashSet<>();

    public Section() {
    }

    public Section(String sectionName, Course course) {
        this.sectionName = sectionName;
        this.course = course;
    }

    // Getters and Setters

    // ID
    public Long getId() {
        return id;
    }

    // Section Name
    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    // Course
    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    // Teacher
    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    // Enrollments
    public Set<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(Set<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    // Grades
//    public Set<Grade> getGrades() {
//        return grades;
//    }
//
//    public void setGrades(Set<Grade> grades) {
//        this.grades = grades;
//    }

    // Assignments
    public Set<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(Set<Assignment> assignments) {
        this.assignments = assignments;
    }

    // Helper Methods to manage bidirectional relationships
    public void addEnrollment(Enrollment enrollment) {
        enrollments.add(enrollment);
        enrollment.setSection(this);
    }

    public void removeEnrollment(Enrollment enrollment) {
        enrollments.remove(enrollment);
        enrollment.setSection(null);
    }


    public void addAssignment(Assignment assignment) {
        assignments.add(assignment);
        assignment.setSection(this);
    }

    public void removeAssignment(Assignment assignment) {
        assignments.remove(assignment);
        assignment.setSection(null);
    }

    // Student Count
    public int getStudentCount() {
        return enrollments.size();
    }
}
