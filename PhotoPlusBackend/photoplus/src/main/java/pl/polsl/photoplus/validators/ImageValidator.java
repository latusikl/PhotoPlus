package pl.polsl.photoplus.validators;

import lombok.SneakyThrows;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import pl.polsl.photoplus.annotations.validators.Image;
import pl.polsl.photoplus.services.controllers.FieldValueExists;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;

/**
 * Checks if file is an image and if it's name is unique.
 */
public class ImageValidator implements ConstraintValidator<Image, MultipartFile> {

    @Autowired
    private ApplicationContext applicationContext;

    private FieldValueExists service;

    @Override
    public void initialize(final Image image) {
        service = this.applicationContext.getBean(image.service());
    }

    @SneakyThrows(IOException.class)
    @Override
    public boolean isValid(final MultipartFile file, final ConstraintValidatorContext context) {
        if (file == null || service.fieldValueExists(file.getOriginalFilename(), "name")) {
            return false;
        }
        final String detectedType = new Tika().detect(file.getBytes());
        return detectedType.startsWith("image");
    }
}

