package pl.polsl.photoplus.controllers.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.photoplus.model.dto.TopicModelDto;
import pl.polsl.photoplus.services.controllers.TopicService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/topic")
public class TopicController extends BaseModelController<TopicModelDto> {

    private final String SECTION_RELATION_NAME = "section";

    public TopicController(final TopicService dtoService) {
        super(dtoService, "topic");
    }

    @Override
    public void addLinks(final TopicModelDto dto) {
        dto.add(linkTo(methodOn(TopicController.class).getSingle(dto.getCode())).withSelfRel());
        dto.add(linkTo(methodOn(TopicController.class).delete(dto.getCode())).withRel(DELETE_RELATION_NAME));
        dto.add(linkTo(methodOn(SectionController.class).delete(dto.getCode())).withRel(SECTION_RELATION_NAME));
    }
}
