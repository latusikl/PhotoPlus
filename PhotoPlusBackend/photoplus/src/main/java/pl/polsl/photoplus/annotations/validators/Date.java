package pl.polsl.photoplus.annotations.validators;

import pl.polsl.photoplus.validators.DateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DateValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Date
{
    String message() default "Date is not valid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
