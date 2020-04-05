package pl.polsl.photoplus.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import pl.polsl.photoplus.annotations.Patchable;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SectionModelDto
        extends AbstractModelDto<SectionModelDto>
{
    @NotBlank(message = "Name is mandatory.")
    @JsonProperty("name")
    @Patchable
    private String name;

    @JsonProperty("description")
    @Patchable
    private String description;

    public SectionModelDto(final String code, final String name, final String description)
    {
        super(code);
        this.name = name;
        this.description = description;
    }
}
