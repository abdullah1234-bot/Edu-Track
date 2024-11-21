package com.fifth_semester.project.dtos.response;

import java.util.List;

public class TeacherResponseDTO {
    private InfoTeacherDTO teacherInfo;
    private List<SectionWithCourseDTO> courses;

    public TeacherResponseDTO(InfoTeacherDTO teacherInfo, List<SectionWithCourseDTO> sections) {
        this.teacherInfo = teacherInfo;
        this.courses = sections;
    }

    public InfoTeacherDTO getTeacherInfo() {
        return teacherInfo;
    }

    public void setTeacherInfo(InfoTeacherDTO teacherInfo) {
        this.teacherInfo = teacherInfo;
    }

    public List<SectionWithCourseDTO> getSections() {
        return courses;
    }

    public void setSections(List<SectionWithCourseDTO> sections) {
        this.courses = sections;
    }

    // Getters and Setters
}
