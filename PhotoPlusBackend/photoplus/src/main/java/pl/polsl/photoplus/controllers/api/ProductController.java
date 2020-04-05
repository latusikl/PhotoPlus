package pl.polsl.photoplus.controllers.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.photoplus.model.dto.ProductModelDto;
import pl.polsl.photoplus.services.controllers.ProductService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/product")
public class ProductController
        extends BaseModelController<ProductModelDto>
{

    private final String CATEGORY_RELATION_NAME = "category";

    public ProductController(final ProductService dtoService)
    {
        super(dtoService);
    }

    @Override
    public void addLinks(final ProductModelDto dto)
    {
        dto.add(linkTo(methodOn(ProductController.class).getSingle(dto.getCode())).withSelfRel());
        dto.add(linkTo(methodOn(ProductController.class).delete(dto.getCode())).withRel(DELETE_RELATION_NAME));
        dto.add(linkTo(methodOn(CategoryController.class).getSingle(dto.getCategory())).withRel(CATEGORY_RELATION_NAME));
    }
}
