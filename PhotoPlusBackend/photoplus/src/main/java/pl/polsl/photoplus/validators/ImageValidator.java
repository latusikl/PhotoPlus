package pl.polsl.photoplus.validators;

import lombok.SneakyThrows;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;
import pl.polsl.photoplus.annotations.validators.Image;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.IOException;

/**
 * Checks if file is an image and if it's name is unique.
 */
public class ImageValidator implements ConstraintValidator<Image, MultipartFile> {

    @SneakyThrows(IOException.class)
    @Override
    public boolean isValid(final MultipartFile file, final ConstraintValidatorContext context) {
        if (file == null ) {
            return false;
        }
        final String detectedType = new Tika().detect(file.getBytes());
        return detectedType.startsWith("image");
    }
}

