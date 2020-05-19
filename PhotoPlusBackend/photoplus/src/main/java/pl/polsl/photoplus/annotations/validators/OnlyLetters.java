package pl.polsl.photoplus.annotations.validators;

import pl.polsl.photoplus.validators.OnlyLetterValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom validator for checking if String contains only letters.
 */
@Documented
@Constraint(validatedBy = OnlyLetterValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.FIELD})
public @interface OnlyLetters
{
    String message() default "Invalid character. Only letters, space and dash are allowed.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
