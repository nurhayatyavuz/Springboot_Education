package com.example.ValidationExmp.annotation;


import com.example.ValidationExmp.validation.AdminValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = AdminValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface NotAdmin {

    String message() default "{isim 'admin' olamaz}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };



}
