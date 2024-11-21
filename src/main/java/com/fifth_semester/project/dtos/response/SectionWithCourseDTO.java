package com.fifth_semester.project.dtos.response;

public class SectionWithCourseDTO {
    private Long sectionId;
    private String sectionName;
    private String courseName;

    public SectionWithCourseDTO(Long sectionId, String sectionName, String courseName) {
        this.sectionId = sectionId;
        this.sectionName = sectionName;
        this.courseName = courseName;
    }


    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
