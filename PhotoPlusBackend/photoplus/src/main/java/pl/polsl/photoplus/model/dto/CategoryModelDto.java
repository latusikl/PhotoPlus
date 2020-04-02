package pl.polsl.photoplus.model.dto;

import lombok.Getter;
import lombok.Setter;
import pl.polsl.photoplus.annotations.validators.OnlyLetters;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
public class CategoryModelDto extends AbstractModelDto<CategoryModelDto> {

    public CategoryModelDto(final String code, final String name) {
        super(code);
        this.name = name;
    }

    @NotBlank(message = "Name is mandatory.")
    @OnlyLetters
    private String name;

}
