package pl.polsl.photoplus.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

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
    @JsonProperty("name")
    private String name;

    @NotBlank(message = "Category code cannot be empty.")
    @JsonProperty("categoryCode")
    private String categoryCode;

    @NotNull(message = "Price is mandatory.")
    @JsonProperty("price")
    private Integer price;

    @JsonProperty("description")
    private String description;

}