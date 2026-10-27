package com.example.ValidationExmp.annotation;

import com.example.ValidationExmp.validation.CiftSayiValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Documented
@Constraint(validatedBy = CiftSayiValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface NotCiftSayi {
    String message() default "{girilen değer çift olmamalıdır}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
