package pl.polsl.photoplus.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SectionModelDto extends AbstractModelDto<SectionModelDto> {
    public SectionModelDto(final String code, final String name, final String description) {
        super(code);
        this.name = name;
        this.description = description;
    }

    @NotBlank(message = "Name is mandatory.")
    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;
}
