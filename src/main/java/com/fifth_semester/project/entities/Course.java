package com.fifth_semester.project.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
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

    // One Course has many Enrollments
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Enrollment> enrollments = new ArrayList<>();

    // One Course has many Grades
//    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Grade> grades = new ArrayList<>();

//    // One Course has many Assignments
//    @OneToMany(mappedBy = "courses", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
//    private List<Assignment> assignments = new ArrayList<>();

    // One Course has many Examinations
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Exam> examinations = new ArrayList<>();

    // One Course has many Sections
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Section> sections = new ArrayList<>();

    // Many Courses can have one Teacher
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    @JsonBackReference
    private Teacher teacher;

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

    // Getters and Setters

    // ID
    public Long getId() {
        return id;
    }

    // Course ID
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    // Course Name
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    // Course Code
    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    // Description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Credit Hours
    public Integer getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(Integer creditHours) {
        this.creditHours = creditHours;
    }

    // Eligible Semester
    public Integer getEligibleSemester() {
        return eligibleSemester;
    }

    public void setEligibleSemester(Integer eligibleSemester) {
        this.eligibleSemester = eligibleSemester;
    }

    // Backlog Eligibility
    public Boolean getBacklogEligible() {
        return isBacklogEligible;
    }

    public void setBacklogEligible(Boolean backlogEligible) {
        isBacklogEligible = backlogEligible;
    }

    // Max Capacity
    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    // Field of Study
    public String getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(String fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    // Semester
    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    // Enrollments
    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    // Examinations
    public List<Exam> getExaminations() {
        return examinations;
    }

    public void setExaminations(List<Exam> examinations) {
        this.examinations = examinations;
    }

    // Sections
    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    // Teacher
    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    // Helper Methods
    public void addSection(Section section) {
        sections.add(section);
        section.setCourse(this);
    }

    public void removeSection(Section section) {
        sections.remove(section);
        section.setCourse(null);
    }

    public void addExam(Exam exam) {
        examinations.add(exam);
        exam.setCourse(this);
    }

    public void removeExam(Exam exam) {
        examinations.remove(exam);
        exam.setCourse(null);
    }

    // Similar helper methods can be added for Enrollments, Grades, Assignments
}
