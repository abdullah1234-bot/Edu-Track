package com.fifth_semester.project.dtos.response;

import java.time.LocalDate;
import java.util.Date;

public class AssignmentDTO {

    private Long assignment_id;
    private String assignmentTitle;
    private String description;
    private Date uploadDate;
    private Date dueDate;
    private boolean submitted;
    private boolean graded;
    private String attachment;
    private String feedback;
    private int marks;
    private StudentInfoDTO student;

    public AssignmentDTO(Long id, String assignmentTitle, String description, Date uploadDate, Date dueDate, boolean submitted, boolean graded, String attachment, String feedback, int marks, StudentInfoDTO studentInfoDTO) {
        this.assignment_id = id;
        this.assignmentTitle = assignmentTitle;
        this.description = description;
        this.uploadDate = uploadDate;
        this.dueDate = dueDate;
        this.submitted = submitted;
        this.graded = graded;
        this.attachment = attachment;
        this.feedback = feedback;
        this.marks = marks;
    }
    public AssignmentDTO(){    }


    public String getAssignmentTitle() {
        return assignmentTitle;
    }

    public void setAssignmentTitle(String assignmentTitle) {
        this.assignmentTitle = assignmentTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }

    public boolean isGraded() {
        return graded;
    }

    public void setGraded(boolean graded) {
        this.graded = graded;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public StudentInfoDTO getStudent() {
        return student;
    }

    public void setStudent(StudentInfoDTO student) {
        this.student = student;
    }

    public Long getAssignment_id() {
        return assignment_id;
    }

    public void setAssignment_id(Long assignment_id) {
        this.assignment_id = assignment_id;
    }

    public static class StudentInfoDTO {
        private String student_id;
        private String username;
        private String emergencyContact;

        public StudentInfoDTO() {
            // Default constructor
        }

        public StudentInfoDTO(String id, String username, String emergencyContact) {
            this.student_id = id;
            this.username = username;
            this.emergencyContact = emergencyContact;
        }

        // Getters and Setters
        public String getStudent_id() {
            return student_id;
        }

        public void setStudent_id(String student_id) {
            this.student_id = student_id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmergencyContact() {
            return emergencyContact;
        }

        public void setEmergencyContact(String emergencyContact) {
            this.emergencyContact = emergencyContact;
        }
    }
}
