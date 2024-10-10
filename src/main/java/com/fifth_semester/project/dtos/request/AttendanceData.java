package com.fifth_semester.project.dtos.request;

public class AttendanceData {

    private Long studentId;
    private boolean isPresent;

    public AttendanceData() {}

    public AttendanceData(Long studentId, boolean isPresent) {
        this.studentId = studentId;
        this.isPresent = isPresent;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }
}
