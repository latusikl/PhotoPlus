package pl.polsl.photoplus.controllers.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.photoplus.model.dto.RatingModelDto;
import pl.polsl.photoplus.security.services.PermissionEvaluatorService;
import pl.polsl.photoplus.services.controllers.RatingService;

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
}
