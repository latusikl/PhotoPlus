package pl.polsl.photoplus.controllers.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.photoplus.model.dto.BatchModelDto;
import pl.polsl.photoplus.services.controllers.BatchService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/batch")
public class BatchController extends BaseModelController<BatchModelDto> {

    private final String PRODUCT_RELATION_NAME = "product";

    public BatchController(final BatchService dtoService) {
        super(dtoService);
    }

    @Override
    public void addLinks(final BatchModelDto dto) {
        dto.add(linkTo(methodOn(BatchController.class).getSingle(dto.getCode())).withSelfRel());
        dto.add(linkTo(methodOn(BatchController.class).delete(dto.getCode())).withRel(DELETE_RELATION_NAME));
        dto.add(linkTo(methodOn(ProductController.class).getSingle(dto.getProductCode())).withRel(PRODUCT_RELATION_NAME));
    }
}
