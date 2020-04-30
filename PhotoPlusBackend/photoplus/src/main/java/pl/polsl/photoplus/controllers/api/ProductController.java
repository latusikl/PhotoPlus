package pl.polsl.photoplus.controllers.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.photoplus.model.dto.ProductModelDto;
import pl.polsl.photoplus.security.services.PermissionEvaluatorService;
import pl.polsl.photoplus.services.controllers.ProductService;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/product")
public class ProductController
        extends BaseModelController<ProductModelDto, ProductService>
{

    private static final String CATEGORY_RELATION_NAME = "category";
    private static final String IMAGE_RELATION_NAME = "image";

    public ProductController(final ProductService dtoService, final PermissionEvaluatorService permissionEvaluatorService)
    {
        super(dtoService, "product", permissionEvaluatorService);
    }

    @GetMapping(produces = {"application/json"})
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(this.authorizationPrefix, 'all' )")
    public ResponseEntity<List<ProductModelDto>> getAllFromCategory(@RequestParam final String categoryCode)
    {
        final List<ProductModelDto> dtos = this.dtoService.getProductsFromCategory(categoryCode);
        addLinks(dtos);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @Override
    public void addLinks(final ProductModelDto dto)
    {
        dto.add(linkTo(methodOn(ProductController.class).getSingle(dto.getCode())).withSelfRel());
        dto.add(linkTo(methodOn(ProductController.class).delete(dto.getCode())).withRel(DELETE_RELATION_NAME));
        dto.add(linkTo(methodOn(CategoryController.class).getSingle(dto.getCategory())).withRel(CATEGORY_RELATION_NAME));
        dto.getImageCodes().forEach(imageCode -> dto.add(linkTo(methodOn(ImageController.class).getSingle(imageCode)).withRel(IMAGE_RELATION_NAME)));
    }

}
