package com.fifth_semester.project.dtos.response;

public class InfoLibrarianDTO {
    private Long id;
    private String username;
    private String email;
    private String employeeId;
    private String librarySection;

    // Constructor
    public InfoLibrarianDTO(Long id, String username, String email, String employeeId, String librarySection) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.employeeId = employeeId;
        this.librarySection = librarySection;
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

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getLibrarySection() {
        return librarySection;
    }

    public void setLibrarySection(String librarySection) {
        this.librarySection = librarySection;
    }
}
