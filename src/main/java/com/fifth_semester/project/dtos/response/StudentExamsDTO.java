package com.fifth_semester.project.dtos.response;

import com.fifth_semester.project.entities.Course;
import com.fifth_semester.project.entities.ExamType;

import java.time.LocalDate;

public class StudentExamsDTO {
    private String examId;
    private LocalDate examDate;
    private String examLocation;
    private int duration;
    private ExamType examType;
    private Long courseId;
    private String courseName; // Optional, for better response

    // Constructor
    public StudentExamsDTO(String examId, LocalDate examDate, String examLocation, int duration, ExamType examType, Long courseId, String courseName) {
        this.examId = examId;
        this.examDate = examDate;
        this.examLocation = examLocation;
        this.duration = duration;
        this.examType = examType;
        this.courseId = courseId;
        this.courseName = courseName;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public LocalDate getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }

    public String getExamLocation() {
        return examLocation;
    }

    public void setExamLocation(String examLocation) {
        this.examLocation = examLocation;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


    public ExamType getExamType() {
        return examType;
    }

    public void setExamType(ExamType examType) {
        this.examType = examType;
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
}
