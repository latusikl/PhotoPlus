package pl.polsl.photoplus.annotations.validators;

import pl.polsl.photoplus.validators.CountryCodeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = CountryCodeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CountryCode
{
    String message() default "Country code is invalid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
