package com.fifth_semester.project.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmergencyContactValidator implements ConstraintValidator<ValidEmergencyContact, String> {

    @Override
    public void initialize(ValidEmergencyContact constraintAnnotation) {}

    @Override
    public boolean isValid(String emergencyContact, ConstraintValidatorContext context) {
        if (emergencyContact == null || emergencyContact.isEmpty()) {
            return true; // Optional field
        }
        return emergencyContact.length() == 11;
    }
}
