package com.fifth_semester.project.dtos.response;

import com.fifth_semester.project.entities.ExamType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ExamResultDTO {
    private Long gradeId;

    // Student Information
    @NotBlank
    @Size(max = 30, message = "Username must be at most 30 characters")
    private String studentId;
    private String studentName;

    // Course Information
    private Long courseId;
    private String courseName;
    private String sectionName;

    // Exam Details
    @NotNull
    private ExamType examType;
    private int marks;
    private String feedback;

    public ExamResultDTO(Long gradeId, String studentId, String studentName,
                    Long courseId, String courseName, String sectionName,
                    ExamType examType, int marks, String feedback) {
        this.gradeId = gradeId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.courseId = courseId;
        this.courseName = courseName;
        this.sectionName = sectionName;
        this.examType = examType;
        this.marks = marks;
        this.feedback = feedback;
    }

    // Getters and Setters

    public Long getGradeId() {
        return gradeId;
    }

    public void setGradeId(Long gradeId) {
        this.gradeId = gradeId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public ExamType getExamType() {
        return examType;
    }

    public void setExamType(ExamType examType) {
        this.examType = examType;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
