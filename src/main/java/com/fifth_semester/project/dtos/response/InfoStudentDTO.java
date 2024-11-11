package com.fifth_semester.project.dtos.response;

import com.fifth_semester.project.entities.BorrowingRecord;
import com.fifth_semester.project.entities.Enrollment;
import com.fifth_semester.project.entities.Fee;
import com.fifth_semester.project.entities.Scholarship;

import java.time.LocalDate;
import java.util.List;

public class InfoStudentDTO {
    private Long id;
    private String username;
    private String email;
    private String studentId;
    private Integer academicYear;
    private int semester;
    private String profilePicture;
    private String emergencyContact;
    private String address;
    private LocalDate dateOfBirth;
//    private List<Enrollment> enrollments;
//    private List<Scholarship> scholarships;
//    private List<InfoAssignment> assignments;
//    private List<Fee> fees;
//    private List<BorrowingRecord> borrowingRecords;

    // Parent fields
    private String parentUsername;
    private String parentEmail;
    private String parentContactNumber;
    private String parentAddress;
    private String parentOccupation;

    // Constructor
    public InfoStudentDTO(Long id, String username, String email, String studentId, Integer academicYear,
                      int semester, String profilePicture, String emergencyContact, String address,
                      LocalDate dateOfBirth,
//             List<Enrollment> enrollments, List<Scholarship> scholarships,List<InfoAssignment> assignments, List<Fee> fees, List<BorrowingRecord> borrowingRecords,
            String parentUsername,String parentEmail, String parentContactNumber,String  parentAddress,String  parentOccupation) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.studentId = studentId;
        this.academicYear = academicYear;
        this.semester = semester;
        this.profilePicture = profilePicture;
        this.emergencyContact = emergencyContact;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
//        this.enrollments = enrollments;
//        this.scholarships = scholarships;
//        this.assignments = assignments;
//        this.fees = fees;
//        this.borrowingRecords = borrowingRecords;
        this.parentUsername = parentUsername;
        this.parentEmail = parentEmail;
        this.parentContactNumber = parentContactNumber;
        this.parentAddress = parentAddress;
        this.parentOccupation = parentOccupation;
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

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
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

//    public List<Enrollment> getEnrollments() {
//        return enrollments;
//    }
//
//    public void setEnrollments(List<Enrollment> enrollments) {
//        this.enrollments = enrollments;
//    }
//
//    public List<Scholarship> getScholarships() {
//        return scholarships;
//    }
//
//    public void setScholarships(List<Scholarship> scholarships) {
//        this.scholarships = scholarships;
//    }
//
//    public List<InfoAssignment> getAssignments() {
//        return assignments;
//    }
//
//    public void setAssignments(List<InfoAssignment> assignments) {
//        this.assignments = assignments;
//    }
//
//    public List<Fee> getFees() {
//        return fees;
//    }
//
//    public void setFees(List<Fee> fees) {
//        this.fees = fees;
//    }
//
//    public List<BorrowingRecord> getBorrowingRecords() {
//        return borrowingRecords;
//    }
//
//    public void setBorrowingRecords(List<BorrowingRecord> borrowingRecords) {
//        this.borrowingRecords = borrowingRecords;
//    }

    public String getParentUsername() {
        return parentUsername;
    }

    public void setParentUsername(String parentUsername) {
        this.parentUsername = parentUsername;
    }

    public String getParentEmail() {
        return parentEmail;
    }

    public void setParentEmail(String parentEmail) {
        this.parentEmail = parentEmail;
    }

    public String getParentContactNumber() {
        return parentContactNumber;
    }

    public void setParentContactNumber(String parentContactNumber) {
        this.parentContactNumber = parentContactNumber;
    }

    public String getParentAddress() {
        return parentAddress;
    }

    public void setParentAddress(String parentAddress) {
        this.parentAddress = parentAddress;
    }

    public String getParentOccupation() {
        return parentOccupation;
    }

    public void setParentOccupation(String parentOccupation) {
        this.parentOccupation = parentOccupation;
    }
}
