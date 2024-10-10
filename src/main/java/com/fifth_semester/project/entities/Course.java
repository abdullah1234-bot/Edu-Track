package com.fifth_semester.project.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String courseId;

    @Column(nullable = false)
    private String courseName;
    private String courseCode;
    private String description;
    private Integer creditHours;
    private Integer eligibleSemester;  // Which semester this course is offered in
    private Boolean isBacklogEligible;
    private Integer maxCapacity;
    private String fieldOfStudy; // e.g., "BCS"
    private Integer semester;
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Grade> grades;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Assignment> assignments;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Exam> examinations;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

    public Course() {}

    public Course(String courseName, String description, Integer creditHours, Integer eligibleSemester, Boolean isBacklogEligible, String fieldOfStudy, Integer semester, Integer maxCapacity) {
        this.courseName = courseName;
        this.description = description;
        this.creditHours = creditHours;
        this.eligibleSemester = eligibleSemester;
        this.isBacklogEligible = isBacklogEligible;
        this.fieldOfStudy = fieldOfStudy;
        this.semester = semester;
        this.maxCapacity = maxCapacity;

    }


    // Getters and setters

    public String getCourseId() {

        return courseId;
    }

    public void setCourseId(String courseId) {

        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public List<Exam> getExaminations() {
        return examinations;
    }

    public void setExaminations(List<Exam> examinations) {
        this.examinations = examinations;
    }

    public Integer getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(Integer creditHours) {
        this.creditHours = creditHours;
    }

    public Integer getEligibleSemester() {
        return eligibleSemester;
    }

    public void setEligibleSemester(Integer eligibleSemester) {
        this.eligibleSemester = eligibleSemester;
    }

    public Boolean getBacklogEligible() {
        return isBacklogEligible;
    }

    public void setBacklogEligible(Boolean backlogEligible) {
        isBacklogEligible = backlogEligible;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
