package pl.polsl.photoplus.validators;

import pl.polsl.photoplus.annotations.validators.Price;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PriceValidator implements ConstraintValidator<Price, Double> {
    public boolean isValid(final Double d, final ConstraintValidatorContext constraintValidatorContext) {
        if(d == null){ return false; }
        final String[] splitter = d.toString().split("\\.");
        return splitter.length == 1 || (splitter[1].length() <= 2 && d > 0.01);
    }
}
