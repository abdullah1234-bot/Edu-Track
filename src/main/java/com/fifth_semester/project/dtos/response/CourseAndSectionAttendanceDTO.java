package com.fifth_semester.project.dtos.response;

import java.time.LocalDate;

public class CourseAndSectionAttendanceDTO {
    private LocalDate attendanceDate;
    private boolean isPresent;
    private String courseName;
    private String username;
    private String sectionName;

    // Constructor
    public CourseAndSectionAttendanceDTO(LocalDate attendanceDate, boolean isPresent, String courseName, String username, String sectionName) {
        this.attendanceDate = attendanceDate;
        this.isPresent = isPresent;
        this.courseName = courseName;
        this.username = username;
        this.sectionName = sectionName;
    }

    // Getters and Setters
    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }
}
