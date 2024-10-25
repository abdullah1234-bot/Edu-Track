package com.fifth_semester.project.dtos.request;

import com.fifth_semester.project.validation.ValidEmergencyContact;
import org.hibernate.validator.constraints.Length;

public class UpdateStudentProfileDTO {
    private String username;
    private String address;

    @ValidEmergencyContact
    private String emergencyContact;

    UpdateStudentProfileDTO(String username, String address, String emergencyContact) {
        this.username = username;
        this.address = address;
        this.emergencyContact = emergencyContact;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public @Length(min = 11, max = 11, message = "Emergency contact must be exactly 11 characters") String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(@Length(min = 11, max = 11, message = "Emergency contact must be exactly 11 characters") String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }
}
