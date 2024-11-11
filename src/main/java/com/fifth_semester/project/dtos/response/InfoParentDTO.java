package com.fifth_semester.project.dtos.response;

import com.fifth_semester.project.entities.Student;

import java.util.List;

public class InfoParentDTO {
    private Long id;
    private String username;
    private String email;
    private String contactNumber;
    private String address;
    private String occupation;
    private List<StudentDTO> children;

    // Constructor
    public InfoParentDTO(Long id, String username, String email, String contactNumber, String address, String occupation) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.contactNumber = contactNumber;
        this.address = address;
        this.occupation = occupation;
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

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public List<StudentDTO> getChildren() {
        return children;
    }

    public void setChildren(List<StudentDTO> children) {
        this.children = children;
    }
}
