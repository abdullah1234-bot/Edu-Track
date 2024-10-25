package com.fifth_semester.project.dtos.response;

public class AssignmentFeedbackDTO {
    private String assignmentTitle;
    private int marks;
    private String feedback;

    public AssignmentFeedbackDTO(String assignmentTitle, int marks, String feedback) {
        this.assignmentTitle = assignmentTitle;
        this.marks = marks;
        this.feedback = feedback;
    }

    // Getters and Setters
    public String getAssignmentTitle() {
        return assignmentTitle;
    }

    public void setAssignmentTitle(String assignmentTitle) {
        this.assignmentTitle = assignmentTitle;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
