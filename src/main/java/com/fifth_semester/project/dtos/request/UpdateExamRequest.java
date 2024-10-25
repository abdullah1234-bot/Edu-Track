package com.fifth_semester.project.dtos.request;

//import com.fifth_semester.project.enums.ExamType;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import com.fifth_semester.project.entities.ExamType;
public class UpdateExamRequest {

    private ExamType examType;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate examDate;

    private String examLocation;

    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer duration; // Changed to Integer to allow null (optional)

    private Long courseId;

    // Constructors
    public UpdateExamRequest() {}

    public UpdateExamRequest(ExamType examType, LocalDate examDate, String examLocation, Integer duration, Long courseId) {
        this.examType = examType;
        this.examDate = examDate;
        this.examLocation = examLocation;
        this.duration = duration;
        this.courseId = courseId;
    }

    // Getters and Setters

    public ExamType getExamType() {
        return examType;
    }

    public void setExamType(ExamType examType) {
        this.examType = examType;
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

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    // toString Method (Optional)
    @Override
    public String toString() {
        return "UpdateExamRequest{" +
                "examType=" + examType +
                ", examDate=" + examDate +
                ", examLocation='" + examLocation + '\'' +
                ", duration=" + duration +
                ", courseId=" + courseId +
                '}';
    }
}
