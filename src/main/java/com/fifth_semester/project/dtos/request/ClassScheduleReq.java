package com.fifth_semester.project.dtos.request;

import java.time.LocalTime;

public class ClassScheduleReq {
    private String courseName;
    private String teacherName;
    private LocalTime startTime;
    private LocalTime endTime;
    private String classroom;
    private int semester;
    private String day;
    private String sectionName;


    public ClassScheduleReq(String courseName, String teacherName, LocalTime StartTime, LocalTime endTime, String classroom, int semester, String day, String section) {
        this.courseName = courseName;
        this.teacherName = teacherName;
        this.startTime = StartTime;
        this.endTime = endTime;
        this.classroom = classroom;
        this.semester = semester;
        this.day = day;
        this.sectionName = section;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
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

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSection(String sectionName) {
        this.sectionName = sectionName;
    }
}
