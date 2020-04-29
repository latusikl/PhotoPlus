package pl.polsl.photoplus.model.dto;

import lombok.Getter;
import lombok.Setter;
import pl.polsl.photoplus.annotations.Patchable;
import pl.polsl.photoplus.annotations.validators.Unique;
import pl.polsl.photoplus.services.controllers.ImageService;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ImageModelDto extends AbstractModelDto<ImageModelDto> {

    public ImageModelDto(final String code, final String name, final byte[] bytes) {
        super(code);
        this.name = name;
        this.bytes = bytes;
    }

    @Patchable
    @NotBlank(message = "Name is mandatory.")
    @Unique(service = ImageService.class, fieldName = "name", fieldNameToBeDisplayed = "Name")
    private String name;

    @Patchable
    @Lob
    private byte[] bytes;
}
