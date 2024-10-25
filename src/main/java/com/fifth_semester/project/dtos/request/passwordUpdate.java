package com.fifth_semester.project.dtos.request;

import jakarta.validation.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class passwordUpdate {
    @NotBlank(message = "Current password is required")
    private String currentPassword;

    @Size(min = 8, message = "New password must be at least 8 characters long")
    private String newPassword;

    public passwordUpdate(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }
    public String getNewPassword() {
        return newPassword;
    }
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
