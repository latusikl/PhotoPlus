package pl.polsl.photoplus.controllers.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.photoplus.model.dto.CategoryModelDto;
import pl.polsl.photoplus.services.controllers.CategoryService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/category")
public class CategoryController extends BaseModelController<CategoryModelDto> {

    public CategoryController(final CategoryService dtoService) {
        super(dtoService);
    }

    @Override
    public void addLinks(final CategoryModelDto dto) {
        dto.add(linkTo(methodOn(CategoryController.class).getSingle(dto.getCode())).withSelfRel());
        dto.add(linkTo(methodOn(CategoryController.class).delete(dto.getCode())).withRel(DELETE_RELATION_NAME));
    }
}
