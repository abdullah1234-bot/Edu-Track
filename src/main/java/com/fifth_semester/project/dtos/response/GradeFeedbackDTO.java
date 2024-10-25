package com.fifth_semester.project.dtos.response;

public class GradeFeedbackDTO {
    private String examType;
    private int marks;
    private String feedback;

    public GradeFeedbackDTO(String examType, int marks, String feedback) {
        this.examType = examType;
        this.marks = marks;
        this.feedback = feedback;
    }

    // Getters and Setters
    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
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

