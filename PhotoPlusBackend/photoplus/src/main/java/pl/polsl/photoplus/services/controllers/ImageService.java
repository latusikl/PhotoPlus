package pl.polsl.photoplus.services.controllers;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.polsl.photoplus.model.dto.ImageModelDto;
import pl.polsl.photoplus.model.entities.Image;
import pl.polsl.photoplus.repositories.ImageRepository;

import java.io.IOException;

@Service
public class ImageService extends AbstractModelService<Image,ImageModelDto, ImageRepository>
        implements FieldValueExists {

    public ImageService(final ImageRepository repository) {
        super(repository);
    }

    @Override
    protected String getModelNameForError() {
        return "image";
    }

    @Override
    protected ImageModelDto getDtoFromModel(final Image modelObject) {
        return new ImageModelDto(modelObject.getCode(), modelObject.getName(), modelObject.getBytes());
    }

    @Override
    protected Image getModelFromDto(final ImageModelDto dtoObject) {
        return new Image(dtoObject.getName(), dtoObject.getBytes());
    }

    @Override
    public boolean fieldValueExists(final String value, final String fieldName) {
        if (fieldName.equals("name")) {
            return this.entityRepository.findByName(value).isPresent();
        }
        return false;
    }

    public boolean isPicture(final MultipartFile file) throws IOException {
        final Tika tika = new Tika();
        final String detectedType = tika.detect(file.getBytes());
        if (detectedType.startsWith("image")) {
            return true;
        }
        return false;
    }
}
