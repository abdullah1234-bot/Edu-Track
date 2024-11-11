package com.fifth_semester.project.dtos.response;

public class InfoAdminDTO {
    private Long id;
    private String username;
    private String email;
    private String adminId;

    // Constructor
    public InfoAdminDTO(Long id, String username, String email, String adminId) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.adminId = adminId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }
}
