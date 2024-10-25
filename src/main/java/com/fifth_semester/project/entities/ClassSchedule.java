package com.fifth_semester.project.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "class_schedules")
public class ClassSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    @JsonManagedReference
    private Course course;  // The course for which the schedule is created

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;  // The teacher assigned for the class

    @Column(nullable = false)
    private LocalTime startTime;  // Start time of the class

    @Column(nullable = false)
    private LocalTime endTime;  // End time of the class

    @Column(nullable = false)
    private String classroom;  // The location where the class is held

    @Column(nullable = false)
    private int semesterNumber;

    @Column(nullable = false)
    private String day;  // The day of the week

    @OneToOne
    @JoinColumn(name = "section_id")
    @JsonManagedReference
    private Section section;

    public ClassSchedule() {}

    public ClassSchedule(Course course, Section section, Teacher teacher, LocalTime startTime, LocalTime endTime, String classroom, int semesterNumber, String day) {
        this.course = course;
        this.teacher = teacher;
        this.section = section;
        this.startTime = startTime;
        this.endTime = endTime;
        this.classroom = classroom;
        this.semesterNumber = semesterNumber;
        this.day = day;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setInstructor(Teacher teacher) {
        this.teacher = teacher;
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

    public void setRoom(String classroom) {
        this.classroom = classroom;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public int getSemesterNumber() {
        return semesterNumber;
    }

    public void setSemester(int semesterNumber) {
        this.semesterNumber = semesterNumber;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
