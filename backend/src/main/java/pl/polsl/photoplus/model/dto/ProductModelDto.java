package pl.polsl.photoplus.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import pl.polsl.photoplus.annotations.Patchable;
import pl.polsl.photoplus.components.ContextProvider;
import pl.polsl.photoplus.model.entities.Category;
import pl.polsl.photoplus.services.controllers.CategoryService;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class ProductModelDto
        extends AbstractModelDto<CategoryModelDto>
{

    @NotBlank(message = "Name is mandatory.")
    @JsonProperty("name")
    @Patchable
    private String name;

    @NotBlank(message = "Category code cannot be empty.")
    @JsonProperty("categoryCode")
    @Patchable(method = "categoryPatch")
    private String category;

    @NotNull(message = "Price is mandatory.")
    @JsonProperty("price")
    @Patchable
    private Integer price;

    @JsonProperty("description")
    @Patchable
    private String description;

    public ProductModelDto(final String code, final String name, final Integer price, final String description, final String category)
    {
        super(code);
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
    }

    public Category categoryPatch()
    {
        final CategoryService categoryService = ContextProvider.getBean(CategoryService.class);
        return categoryService.findByCodeOrThrowError(category, "TopicPatchSection");
    }
}