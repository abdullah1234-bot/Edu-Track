package com.fifth_semester.project.dtos.response;

import java.util.List;

public class BulkUploadResponse {
    private String message;
    private int totalRecords;
    private int successfulRecords;
    private int failedRecords;
    private List<String> errorDetails;

    public BulkUploadResponse(String message, int totalRecords, int successfulRecords, int failedRecords, List<String> errorDetails) {
        this.message = message;
        this.totalRecords = totalRecords;
        this.successfulRecords = successfulRecords;
        this.failedRecords = failedRecords;
        this.errorDetails = errorDetails;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getSuccessfulRecords() {
        return successfulRecords;
    }

    public void setSuccessfulRecords(int successfulRecords) {
        this.successfulRecords = successfulRecords;
    }

    public int getFailedRecords() {
        return failedRecords;
    }

    public void setFailedRecords(int failedRecords) {
        this.failedRecords = failedRecords;
    }

    public List<String> getErrorDetails() {
        return errorDetails;
    }

    public void setErrorDetails(List<String> errorDetails) {
        this.errorDetails = errorDetails;
    }
}
