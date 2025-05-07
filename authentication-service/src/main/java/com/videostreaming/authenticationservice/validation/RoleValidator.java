package com.videostreaming.authenticationservice.validation;

import java.util.Set;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RoleValidator implements ConstraintValidator<ValidRole, String>{

    private static final Set<String> ALLOWED_ROLES = Set.of("USER", "admin","user","ADMIN","User","Admin");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && ALLOWED_ROLES.contains(value);
    }

}
