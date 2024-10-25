package com.fifth_semester.project.dtos.response;

public class StudentDTO {
    private Long id;
    private String email;
    private String studentId;
    private String username;

    public StudentDTO(Long id, String email, String studentId, String username) {
        this.id = id;
        this.email = email;
        this.studentId = studentId;
        this.username = username;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

