package com.example.ValidationExmp.validation;

import com.example.ValidationExmp.annotation.NotAdmin;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AdminValidator implements ConstraintValidator<NotAdmin,String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context){
        if(value==null){
            return true;
        }
        return !value.equalsIgnoreCase("admin");
    }

}
