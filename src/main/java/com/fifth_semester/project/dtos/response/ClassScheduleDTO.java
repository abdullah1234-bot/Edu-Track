package com.fifth_semester.project.dtos.response;

import java.time.LocalTime;

public class ClassScheduleDTO {
    private Long id;
    private Long courseId;
    private String courseName;
    private String courseCode;
    private String sectionName;
    private Long teacherId;
    private String teacherName;
    private LocalTime startTime;
    private LocalTime endTime;
    private String classroom;
    private String day;

    // Constructors
    public ClassScheduleDTO() {}

    public ClassScheduleDTO(Long id, Long courseId, String courseName, String courseCode,
                            String sectionName, Long teacherId, String teacherName,
                            LocalTime startTime, LocalTime endTime, String classroom, String day) {
        this.id = id;
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.sectionName = sectionName;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.classroom = classroom;
        this.day = day;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    // ... [Other getters and setters] ...

    public void setId(Long id) {
        this.id = id;
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

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
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

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
