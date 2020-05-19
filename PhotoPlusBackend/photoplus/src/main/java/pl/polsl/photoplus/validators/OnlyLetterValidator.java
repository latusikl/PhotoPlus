package pl.polsl.photoplus.validators;


import pl.polsl.photoplus.annotations.validators.OnlyLetters;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class OnlyLetterValidator implements ConstraintValidator<OnlyLetters, String> {


   public boolean isValid(final String s, final ConstraintValidatorContext constraintValidatorContext) {
     return s != null ? Pattern.matches("^[\\p{IsAlphabetic}][\\p{IsAlphabetic}\\p{Space}-]*[\\p{IsAlphabetic}]+$", s) : false;
   }
}
