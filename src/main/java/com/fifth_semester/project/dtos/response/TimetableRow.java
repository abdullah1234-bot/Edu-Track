package com.fifth_semester.project.dtos.response;

import java.time.LocalDate;
import java.time.LocalTime;

public class TimetableRow {
    private String courseCode;  // e.g., DAA
    private String section;     // e.g., BCS-5C
    private int semester;
    private String instructor;  // e.g., Anaum Hamid
    private String timeSlot;    // e.g., 08:00-8:55
    private String venue;       // e.g., E-1 Academic Block I
    private LocalTime startTime; //e.g., 08:00
    private LocalTime endTime; //e.g., 08:55
    private String day; //e.g., monday
    public TimetableRow(String courseCode, String section, String instructor, int semester, String timeSlot, String venue, LocalTime startTime, LocalTime endTime, String day) {
        this.courseCode = courseCode;
        this.section = section;
        this.instructor = instructor;
        this.semester = semester;
        this.timeSlot = timeSlot;
        this.venue = venue;
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
    }
    // Getters and setters
    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }
}
