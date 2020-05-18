package pl.polsl.photoplus.annotations.validators;

import pl.polsl.photoplus.services.controllers.FieldValueExists;
import pl.polsl.photoplus.validators.UniqueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Unique
{
    String message() default "{fieldNameToBeDisplayed} already in use.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends FieldValueExists> service();

    String fieldName() default "";

    String fieldNameToBeDisplayed() default "";
}
