package com.fifth_semester.project.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "admins")
public class Admin extends User {

    @Column(unique = true)
    private String adminId;

    public Admin() {}

    public Admin(String username, String email, String password, String adminId) {
        super(username, email, password);
        this.adminId = adminId;
    }

    // Getters and setters

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

}
