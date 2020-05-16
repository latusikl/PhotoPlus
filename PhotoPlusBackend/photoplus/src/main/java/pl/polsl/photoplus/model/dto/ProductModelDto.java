package pl.polsl.photoplus.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import pl.polsl.photoplus.annotations.Patchable;
import pl.polsl.photoplus.components.ContextProvider;
import pl.polsl.photoplus.model.entities.Category;
import pl.polsl.photoplus.model.entities.Image;
import pl.polsl.photoplus.services.controllers.CategoryService;
import pl.polsl.photoplus.services.controllers.ImageService;

import javax.persistence.ElementCollection;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @DecimalMin("0.01")
    @Patchable
    private Double price;

    @JsonProperty("description")
    @Patchable
    private String description;

    @JsonProperty("storeQuantity")
    @Min(value = 0, message ="Store quantity cannot be less than zero.")
    @Patchable
    private Integer storeQuantity;

    @JsonProperty("imageCodes")
    @Patchable(method = "imagePatch")
    private List<String> images;

    @JsonProperty("dataLinks")
    @Patchable
    private Map<String, String> dataLinks;

    public ProductModelDto(final String code, final String name, final Double price, final String description,
                           final String category, final Integer storeQuantity, final List<String> images,
                           final Map<String, String> dataLinks)
    {
        super(code);
        this.name = name;
        this.price = price;
        this.description = description;
        this.category = category;
        this.images = images;
        this.storeQuantity = storeQuantity;
        this.dataLinks = dataLinks;
    }

    @JsonIgnore
    public Category categoryPatch()
    {
        final CategoryService categoryService = ContextProvider.getBean(CategoryService.class);
        return categoryService.findByCodeOrThrowError(category, "CategoryPatchSection");
    }

    @JsonIgnore
    public List<Image> imagePatch()
    {
        final ImageService imageService = ContextProvider.getBean(ImageService.class);
        final List<Image> imageList = new ArrayList<>();
        for (final String imageCode : this.images) {
            imageList.add(imageService.findByCodeOrThrowError(imageCode, "ImagesPatch"));
        }
        return imageList;
    }
}