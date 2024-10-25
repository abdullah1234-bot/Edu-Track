package com.fifth_semester.project.dtos.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class AssignTeacherRequest {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Teacher email is required")
    private String teacherEmail;

    @NotBlank(message = "Course name is required")
    private String courseName;

    @NotBlank(message = "Section name is required")
    private String sectionName;

    // Constructors
    public AssignTeacherRequest() {}

    public AssignTeacherRequest(String teacherEmail, String courseName, String sectionName) {
        this.teacherEmail = teacherEmail;
        this.courseName = courseName;
        this.sectionName = sectionName;
    }

    // Getters and Setters
    public String getTeacherEmail() {
        return teacherEmail;
    }

    public void setTeacherEmail(String teacherEmail) {
        this.teacherEmail = teacherEmail;
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
}
