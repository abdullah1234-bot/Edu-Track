package com.fifth_semester.project.dtos.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AttendanceData {

    @NotNull(message = "Student ID cannot be null")
    @Min(value = 1, message = "Student ID must be a positive number")
    private Long studentId;

    @NotNull(message = "Attendance status cannot be null")
    private boolean status;

    public AttendanceData() {}

    public AttendanceData(Long studentId, boolean status) {
        this.studentId = studentId;
        this.status = status;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    // Correct Getter Following JavaBean Naming Conventions
    public boolean getStatus() {
        return status;
    }

    // Setter remains unchanged
    public void setStatus(boolean status) {
        this.status = status;
    }
}
