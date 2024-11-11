package com.fifth_semester.project.dtos.response;

import java.time.LocalDate;
import java.util.Date;

public class InfoAssignment {

    private Long assignment_id;
    private String assignmentTitle;
    private String description;
    private LocalDate uploadDate;
    private LocalDate dueDate;
    private boolean submitted;
    private boolean graded;
    private String attachment;
    private String feedback;
    private int marks;


    public InfoAssignment(Long id,
                          String assignmentTitle,
                          String description,
                          LocalDate uploadDate,    // Changed from LocalDate
                          LocalDate dueDate,       // Changed from LocalDate
                          boolean submitted,
                          boolean graded,
                          String attachment,
                          String feedback,
                          int marks) {
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

    public InfoAssignment() {}


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

    public LocalDate getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDate uploadDate) {
        this.uploadDate = uploadDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
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


    public Long getAssignment_id() {
        return assignment_id;
    }

    public void setAssignment_id(Long assignment_id) {
        this.assignment_id = assignment_id;
    }


}
