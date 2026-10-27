package com.example.ValidationExmp.validation;


import com.example.ValidationExmp.annotation.ConfirmPassword;
import com.example.ValidationExmp.dto.UserRegisterDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ConfirmPasswordValidator implements ConstraintValidator<ConfirmPassword, UserRegisterDto> {


    @Override
    public boolean isValid(UserRegisterDto userRegisterDto, ConstraintValidatorContext context) {
        if(userRegisterDto.getPassword()==null || userRegisterDto.getConfirmPassword()==null){
            return  false;

        }
        return userRegisterDto.getPassword().equals(userRegisterDto.getConfirmPassword());
    }
}
