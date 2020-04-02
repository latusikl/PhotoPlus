package pl.polsl.photoplus.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import pl.polsl.photoplus.annotations.Patchable;
import pl.polsl.photoplus.model.entities.Category;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class ProductModelDto extends AbstractModelDto<CategoryModelDto> {

    public ProductModelDto(final String code, final String name, final Integer price, final String description,
                           final String categoryCode) {
        super(code);
        this.name = name;
        this.price = price;
        this.description = description;
        this.categoryCode = categoryCode;
    }

    @NotBlank(message = "Name is mandatory.")
    private String name;

    @NotBlank(message = "Category code cannot be empty.")
    private String categoryCode;

    @NotNull(message = "Price is mandatory.")
    private Integer price;

    private String description;

}