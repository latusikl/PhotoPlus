package pl.polsl.photoplus.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import pl.polsl.photoplus.annotations.validators.Unique;
import pl.polsl.photoplus.services.controllers.FieldValueExists;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueValidator implements ConstraintValidator<Unique, String> {

    @Autowired
    private ApplicationContext applicationContext;

    private String fieldName;
    private FieldValueExists service;

    @Override
    public void initialize(final Unique unique) {
        service = this.applicationContext.getBean(unique.service());
        this.fieldName = unique.fieldName();
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        return !service.fieldValueExists(value, fieldName);
    }
}
