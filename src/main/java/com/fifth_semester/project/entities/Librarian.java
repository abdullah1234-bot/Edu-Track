package com.fifth_semester.project.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "librarians")
public class Librarian extends User {

    private String employeeId;
    private String librarySection;

    public Librarian() {}

    public Librarian(String username, String email, String password, String employeeId, String librarySection) {
        super(username, email, password);
        this.employeeId = employeeId;
        this.librarySection = librarySection;
    }

    // Getters and setters

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
