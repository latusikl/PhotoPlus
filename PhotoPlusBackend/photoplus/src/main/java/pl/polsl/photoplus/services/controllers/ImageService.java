package pl.polsl.photoplus.services.controllers;

import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.dto.ImageModelDto;
import pl.polsl.photoplus.model.entities.Image;
import pl.polsl.photoplus.repositories.ImageRepository;

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

}
