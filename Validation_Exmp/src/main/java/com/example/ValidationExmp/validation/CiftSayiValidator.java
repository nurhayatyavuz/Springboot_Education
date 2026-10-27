package com.example.ValidationExmp.validation;

import com.example.ValidationExmp.annotation.NotCiftSayi;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CiftSayiValidator implements ConstraintValidator<NotCiftSayi, Integer> {

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return value % 2 != 0;
    }
}

