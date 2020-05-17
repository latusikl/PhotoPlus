package pl.polsl.photoplus.controllers.api;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.polsl.photoplus.model.dto.RatingModelDto;
import pl.polsl.photoplus.security.services.PermissionEvaluatorService;
import pl.polsl.photoplus.services.controllers.RatingService;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/rating")
public class RatingController extends BaseModelController<RatingModelDto,RatingService> {

    private final String PRODUCT_RELATION_NAME = "product";
    private final String CUSTOMER_RELATION_NAME = "customer";

    public RatingController(final RatingService dtoService, final PermissionEvaluatorService permissionEvaluatorService) {
        super(dtoService, "rating", permissionEvaluatorService);
    }

    @Override
    public void addLinks(final RatingModelDto dto) {
        dto.add(linkTo(methodOn(RatingController.class).getSingle(dto.getCode())).withSelfRel());
        dto.add(linkTo(methodOn(RatingController.class).delete(dto.getCode())).withRel(DELETE_RELATION_NAME));
        dto.add(linkTo(methodOn(UserController.class).getSingle(dto.getUserCode())).withRel(CUSTOMER_RELATION_NAME));
        dto.add(linkTo(methodOn(UserController.class).getSingle(dto.getUserCode())).withRel(PRODUCT_RELATION_NAME));
    }

    @GetMapping(path = "/all/{page}", produces = {"application/json"}, params = {"sortedBy", "productCode"})
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, this.authorizationPrefix, 'all' )")
    public ResponseEntity<List<RatingModelDto>> getAllByProductCode(@PathVariable final Integer page,
                                                                    @RequestParam final String sortedBy,
                                                                    @RequestParam final String productCode)
    {
        final List<RatingModelDto> dtos = dtoService.getPageByProductCode(page, sortedBy, productCode);
        addLinks(dtos);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping(path = "/page/count", produces = {"application/json"}, params = "productCode")
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, this.authorizationPrefix, 'all' )")
    public ResponseEntity<ObjectNode> getAmountOfPages(@RequestParam final String productCode)
    {
        return new ResponseEntity<>(dtoService.getPageCountOfRatingByProductCode(productCode), HttpStatus.OK);
    }
}
