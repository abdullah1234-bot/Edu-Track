package com.fifth_semester.project.dtos.response;

import com.fifth_semester.project.entities.ExamType;
import com.fifth_semester.project.entities.GradeValue;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class GradeDTO {
    private Long id;
    private Long courseId;
    private String StudentId;
    private String StudentName;
    private String courseName;
    private String sectionName;
    @NotNull
    @Enumerated(EnumType.STRING) // or EnumType.ORDINAL if you're storing as integers
    private ExamType examType;
    private int marks;
    private String feedback;
    private GradeValue grade;

    public GradeDTO() {}
    public GradeDTO(Long id,String studentId,String studentName,Long courseId,String courseName,String SectionName, ExamType examType, int marks, String feedback, GradeValue grade) {
        this.id = id;
        this.StudentId = studentId;
        this.StudentName = studentName;
        this.courseId = courseId;
        this.courseName = courseName;
        this.sectionName = SectionName;
        this.examType = examType;
        this.marks = marks;
        this.feedback = feedback;
        this.grade = grade;
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

    public @NotNull ExamType getExamType() {
        return examType;
    }

    public void setExamType(@NotNull ExamType examType) {
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

    public String getStudentId() {
        return StudentId;
    }

    public void setStudentId(String studentId) {
        StudentId = studentId;
    }

    public String getStudentName() {
        return StudentName;
    }

    public void setStudentName(String studentName) {
        StudentName = studentName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public GradeValue getGrade() {
        return grade;
    }

    public void setGrade(GradeValue grade) {
        this.grade = grade;
    }
}
