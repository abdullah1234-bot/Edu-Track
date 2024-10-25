package com.fifth_semester.project.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "students")
public class Student extends User {
    @Column(unique = true)
    private String studentId; // Changed from @Id to regular field

    private Integer academicYear;
    private int semester;

    private String profilePicture;

    private String emergencyContact;

    private String address;

    private LocalDate dateOfBirth;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Enrollment> enrollments;

//    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonManagedReference
//    private List<Grade> grades;
@OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
@JsonManagedReference
private List<Scholarship> scholarships;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Assignment> assignments;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Fee> fees;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<BorrowingRecord> borrowingRecords;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonBackReference
    private Parent parent;
//
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(
//            name = "student_courses",
//            joinColumns = @JoinColumn(name = "student_id"),
//            inverseJoinColumns = @JoinColumn(name = "course_id")
//    )
//    private Set<Course> coursesEnrolled;

    public Student() {}

    public Student(String username, String email, String password, Integer academicYear, int semester) {
        super(username, email, password);
        this.academicYear = academicYear;
        this.semester = semester;
    }

    // Getters and setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public Integer getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(Integer academicYear) {
        this.academicYear = academicYear;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<Fee> getFees() {
        return fees;
    }

    public void setFees(List<Fee> fees) {
        this.fees = fees;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public List<Course> getCoursesEnrolled() {
        return enrollments.stream()
                .map(Enrollment::getCourse)
                .collect(Collectors.toList());
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    public List<Scholarship> getScholarships() {
        return scholarships;
    }

    public void setScholarships(List<Scholarship> scholarships) {
        this.scholarships = scholarships;
    }
}
