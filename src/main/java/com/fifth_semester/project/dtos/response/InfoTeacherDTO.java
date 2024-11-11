package com.fifth_semester.project.dtos.response;

import com.fifth_semester.project.entities.Course;
import com.fifth_semester.project.entities.Section;

import java.time.LocalDate;
import java.util.List;
public class InfoTeacherDTO {
    private Long id;
    private String username;
    private String email;
    private String teacherId;
    private String department;
    private String officeHours;
    private LocalDate dateOfHire;
    private String qualification;
    private String specialization;
//    private List<Section> sections;
//    private List<CourseDTO> courses;
//    private List<ClassScheduleDTO> classSchedules;

    // Constructor
    public InfoTeacherDTO(Long id, String username, String email, String teacherId, String department,
                      String officeHours, LocalDate dateOfHire, String qualification,
                      String specialization
//                          ,List<Section> sections,
//                      List<CourseDTO> courses, List<ClassScheduleDTO> classSchedules
                      ){
        this.id = id;
        this.username = username;
        this.email = email;
        this.teacherId = teacherId;
        this.department = department;
        this.officeHours = officeHours;
        this.dateOfHire = dateOfHire;
        this.qualification = qualification;
        this.specialization = specialization;
//        this.sections = sections;
//        this.courses = courses;
//        this.classSchedules = classSchedules;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOfficeHours() {
        return officeHours;
    }

    public void setOfficeHours(String officeHours) {
        this.officeHours = officeHours;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public LocalDate getDateOfHire() {
        return dateOfHire;
    }

    public void setDateOfHire(LocalDate dateOfHire) {
        this.dateOfHire = dateOfHire;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

//    public List<Section> getSections() {
//        return sections;
//    }
//
//    public void setSections(List<Section> sections) {
//        this.sections = sections;
//    }
//
//    public List<CourseDTO> getCourses() {
//        return courses;
//    }
//
//    public void setCourses(List<CourseDTO> courses) {
//        this.courses = courses;
//    }
//
//    public List<ClassScheduleDTO> getClassSchedules() {
//        return classSchedules;
//    }
//
//    public void setClassSchedules(List<ClassScheduleDTO> classSchedules) {
//        this.classSchedules = classSchedules;
//    }
}
