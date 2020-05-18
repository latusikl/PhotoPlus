package pl.polsl.photoplus.validators;

import pl.polsl.photoplus.annotations.validators.Unique;
import pl.polsl.photoplus.components.ContextProvider;
import pl.polsl.photoplus.services.controllers.FieldValueExists;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueValidator
        implements ConstraintValidator<Unique,String>
{
    private String fieldName;

    private FieldValueExists service;

    @Override
    public void initialize(final Unique unique)
    {
        service = ContextProvider.getBean(unique.service());
        this.fieldName = unique.fieldName();
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context)
    {
        return !service.fieldValueExists(value, fieldName);
    }
}
