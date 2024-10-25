package com.fifth_semester.project.dtos.response;

import com.fifth_semester.project.entities.ExamType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class StudentExamResultDTO {
    private Long courseId;
    private String courseName;
    @NotNull
    private ExamType examType;
    private int marks;
    private String feedback;

    public StudentExamResultDTO(Long courseId,String courseName,ExamType examType, int marks, String feedback) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.examType = examType;
        this.marks = marks;
        this.feedback = feedback;
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

    public @NotNull ExamType getExamType() {
        return examType;
    }

    public void setExamType(@NotNull ExamType examType) {
        this.examType = examType;
    }
}
