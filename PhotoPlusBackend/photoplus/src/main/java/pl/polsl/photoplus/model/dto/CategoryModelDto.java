package pl.polsl.photoplus.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import pl.polsl.photoplus.annotations.Patchable;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class CategoryModelDto
        extends AbstractModelDto<CategoryModelDto>
{

    @NotBlank(message = "Name is mandatory.")
    @JsonProperty("name")
    @Patchable
    private String name;

    public CategoryModelDto(final String code, final String name)
    {
        super(code);
        this.name = name;
    }

}
