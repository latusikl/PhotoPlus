package pl.polsl.photoplus.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import pl.polsl.photoplus.annotations.Patchable;

import javax.persistence.Lob;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ImageModelDto extends AbstractModelDto<ImageModelDto> {

    public ImageModelDto(final String code, final String name, final byte[] bytes, final String product) {
        super(code);
        this.name = name;
        this.bytes = bytes;
        this.product = product;
    }

    @Patchable
    @NotBlank(message = "Name is mandatory.")
    private String name;

    @Patchable
    @Lob
    private byte[] bytes;

    @Patchable
    @JsonProperty("productCode")
    private String product;
}
