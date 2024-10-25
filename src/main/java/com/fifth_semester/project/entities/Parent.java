package com.fifth_semester.project.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "parents")
public class Parent extends User {

    private String contactNumber;
    private String address;
    private String occupation;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Student> children;  // List of children (students) associated with the parent

    public Parent() {}

    public Parent(String username, String email, String password, String contactNumber, String address, String occupation) {
        super(username, email, password);  // Call the User constructor for shared fields
        this.contactNumber = contactNumber;
        this.address = address;
        this.occupation = occupation;
    }

    // Getters and setters for Parent-specific fields


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

    public List<Student> getChildren() {
        return children;
    }

    public void setChildren(List<Student> children) {
        this.children = children;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
