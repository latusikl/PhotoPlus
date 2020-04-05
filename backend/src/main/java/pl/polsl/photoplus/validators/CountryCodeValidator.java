package pl.polsl.photoplus.validators;

import pl.polsl.photoplus.annotations.validators.CountryCode;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Locale;
import java.util.Set;

public class CountryCodeValidator implements ConstraintValidator<CountryCode,String>
{
    private static final Set<String> validCountryCodes = Set.of(Locale.getISOCountries());

    @Override
    public boolean isValid(final String s, final ConstraintValidatorContext constraintValidatorContext)
    {
        return s != null ? validCountryCodes.contains(s) : false;
    }
}
