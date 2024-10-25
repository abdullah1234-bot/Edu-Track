package com.fifth_semester.project.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "enrollments")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many Enrollments belong to one Student
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @JsonBackReference
    private Student student;

    // Many Enrollments belong to one Course
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    @JsonBackReference
    private Course course;

    // Many Enrollments belong to one Section
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    @JsonBackReference
    private Section section;

    private Integer semester;
    private Boolean isBacklog = false;
    private Boolean cleared = false;

    @OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Attendance> attendances;

    // One Enrollment has many Grades
    @OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Grade> grades;

    public Enrollment() {}

    public Enrollment(Student student, Course course, Section section, Integer semester, Boolean isBacklog, Boolean cleared) {
        this.student = student;
        this.course = course;
        this.section = section;
        this.semester = semester;
        this.isBacklog = isBacklog;
        this.cleared = cleared;
    }

    // Getters and Setters

    // ID
    public Long getId() {
        return id;
    }

    // Student
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    // Course
    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    // Section
    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    // Semester
    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    // Is Backlog
    public Boolean getIsBacklog() {
        return isBacklog;
    }

    public void setIsBacklog(Boolean isBacklog) {
        this.isBacklog = isBacklog;
    }

    // Cleared
    public Boolean getCleared() {
        return cleared;
    }

    public void setCleared(Boolean cleared) {
        this.cleared = cleared;
    }

    // Attendances
    public List<Attendance> getAttendances() {
        return attendances;
    }

    public void setAttendances(List<Attendance> attendances) {
        this.attendances = attendances;
    }

    // Grades
    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    // Helper Methods
    public void addGrade(Grade grade) {
        grades.add(grade);
        grade.setEnrollment(this);
    }

    public void removeGrade(Grade grade) {
        grades.remove(grade);
        grade.setEnrollment(null);
    }
}
