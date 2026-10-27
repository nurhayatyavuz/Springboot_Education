package com.example.ValidationExmp.annotation;

import com.example.ValidationExmp.validation.ConfirmPasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ConfirmPasswordValidator.class)
@Target(ElementType.TYPE)
@Retention(RUNTIME)
public @interface ConfirmPassword {
    String message() default "şifreler eşleşmiyor...";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
