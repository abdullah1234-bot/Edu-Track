package com.fifth_semester.project.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "class_schedules")
public class ClassSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;  // The course for which the schedule is created

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;  // The teacher assigned for the class

    @Column(nullable = false)
    private LocalDate classDate;  // The date of the class

    @Column(nullable = false)
    private LocalTime startTime;  // Start time of the class

    @Column(nullable = false)
    private LocalTime endTime;  // End time of the class

    @Column(nullable = false)
    private String classroom;  // The location where the class is held

    @Column(nullable = false)
    private int semesterNumber;

    @OneToOne
    @JoinColumn(name = "section_id")
    private Section section;


    public ClassSchedule() {}

    public ClassSchedule(Course course, Section section, Teacher teacher, LocalDate classDate, LocalTime startTime, LocalTime endTime, String classroom, int semesterNumber) {
        this.course = course;
        this.teacher = teacher;
        this.section = section;
        this.classDate = classDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.classroom = classroom;
        this.semesterNumber = semesterNumber;
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

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public LocalDate getClassDate() {
        return classDate;
    }

    public void setClassDate(LocalDate classDate) {
        this.classDate = classDate;
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

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public int getSemesterNumber() {
        return semesterNumber;
    }
}
