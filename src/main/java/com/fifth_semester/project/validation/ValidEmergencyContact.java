package com.fifth_semester.project.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EmergencyContactValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmergencyContact {
    String message() default "Emergency contact must be exactly 11 characters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
