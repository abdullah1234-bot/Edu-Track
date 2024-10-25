package com.fifth_semester.project.dtos.response;

import java.time.LocalTime;

public class TeacherClassScheduleDTO {
    private Long id;
    private String classroom;
    private String courseName;
    private String day;
    private LocalTime startTime;
    private LocalTime endTime;
    private String sectionName;

    // Constructors

    public TeacherClassScheduleDTO() {}

    public TeacherClassScheduleDTO(Long id, String classroom, String courseName, String day,
                            LocalTime startTime, LocalTime endTime, String sectionName) {
        this.id = id;
        this.classroom = classroom;
        this.courseName = courseName;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.sectionName = sectionName;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    // (Similarly, add getters and setters for all fields)

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

}
