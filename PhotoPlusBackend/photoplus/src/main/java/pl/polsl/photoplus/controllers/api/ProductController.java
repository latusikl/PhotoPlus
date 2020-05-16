package pl.polsl.photoplus.controllers.api;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping(path = "/{page}", produces = {"application/json"}, params = "categoryCode")
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, this.authorizationPrefix, 'all' )")
    public ResponseEntity<List<ProductModelDto>> getPageFromCategory(@PathVariable("page") final Integer page,
                                                                    @RequestParam final String categoryCode)
    {
        final List<ProductModelDto> dtos = this.dtoService.getProductsFromCategory(page, categoryCode);
        addLinks(dtos);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping(path = "/page/count", produces = "application/json", params = "categoryCode")
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, this.authorizationPrefix, 'all' )")
    public ResponseEntity<ObjectNode> getAmountOfPages(@RequestParam final String categoryCode)
    {
        return new ResponseEntity<>(dtoService.getPageCountOfProductFromCategory(categoryCode), HttpStatus.OK);
    }

    @Override
    @GetMapping(path = "/all/{page}", produces = {"application/json"})
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, this.authorizationPrefix, 'all' )")
    public ResponseEntity<List<ProductModelDto>> getAll(@PathVariable("page") final Integer page)
    {
        final List<ProductModelDto> dtos = dtoService.getPageFromAll(page, "name");
        addLinks(dtos);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping(path = "/top", produces = {"application/json"})
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, this.authorizationPrefix, 'all' )")
    public ResponseEntity<List<ProductModelDto>> getTop()
    {
        final List<ProductModelDto> dtos = dtoService.getTopEight();
        addLinks(dtos);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping(path = "/all/{page}", produces = {"application/json"}, params = "sortedBy")
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, this.authorizationPrefix, 'all' )")
    public ResponseEntity<List<ProductModelDto>> getAll(@PathVariable final Integer page,
                                                               @RequestParam final String sortedBy)
    {
        final List<ProductModelDto> dtos;
        dtos = dtoService.getPageFromAll(page, sortedBy);
        addLinks(dtos);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @Override
    public void addLinks(final ProductModelDto dto)
    {
        dto.add(linkTo(methodOn(ProductController.class).getSingle(dto.getCode())).withSelfRel());
        dto.add(linkTo(methodOn(ProductController.class).delete(dto.getCode())).withRel(DELETE_RELATION_NAME));
        dto.add(linkTo(methodOn(CategoryController.class).getSingle(dto.getCategory())).withRel(CATEGORY_RELATION_NAME));
        dto.getImages().forEach(imageCode -> dto.add(linkTo(methodOn(ImageController.class).getSingle(imageCode)).withRel(IMAGE_RELATION_NAME)));
    }

    @GetMapping(path = {"/search"}, produces = {"application/json"}, params = "str")
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, this.authorizationPrefix, 'all' )")
    public ResponseEntity searchByLogin(@RequestParam final String str)
    {
        return new ResponseEntity(dtoService.getByNameContainingStr(str), HttpStatus.OK);
    }

}
