package pl.polsl.photoplus.controllers.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.photoplus.model.dto.SectionModelDto;
import pl.polsl.photoplus.services.controllers.SectionService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/section")
public class SectionController extends BaseModelController<SectionModelDto> {

    public SectionController(final SectionService dtoService) {
        super(dtoService, "section");
    }

    @Override
    public void addLinks(final SectionModelDto dto) {
        dto.add(linkTo(methodOn(SectionController.class).getSingle(dto.getCode())).withSelfRel());
        dto.add(linkTo(methodOn(SectionController.class).delete(dto.getCode())).withRel(DELETE_RELATION_NAME));
    }
}
