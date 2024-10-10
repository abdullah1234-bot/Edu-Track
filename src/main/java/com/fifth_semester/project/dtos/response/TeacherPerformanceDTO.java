package com.fifth_semester.project.dtos.response;

import com.fifth_semester.project.entities.Teacher;

public class TeacherPerformanceDTO {

    private Teacher teacher;
    private int totalCourses;
    private int totalEnrollments;

    public TeacherPerformanceDTO(Teacher teacher, int totalCourses, int totalEnrollments) {
        this.teacher = teacher;
        this.totalCourses = totalCourses;
        this.totalEnrollments = totalEnrollments;
    }

    // Getters and setters
    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public int getTotalCourses() {
        return totalCourses;
    }

    public void setTotalCourses(int totalCourses) {
        this.totalCourses = totalCourses;
    }

    public int getTotalEnrollments() {
        return totalEnrollments;
    }

    public void setTotalEnrollments(int totalEnrollments) {
        this.totalEnrollments = totalEnrollments;
    }
}
