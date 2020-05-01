package pl.polsl.photoplus.controllers.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.photoplus.model.dto.TopicModelDto;
import pl.polsl.photoplus.security.services.PermissionEvaluatorService;
import pl.polsl.photoplus.services.controllers.TopicService;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/topic")
public class TopicController extends BaseModelController<TopicModelDto,TopicService> {

    private final String SECTION_RELATION_NAME = "section";

    public TopicController(final TopicService dtoService, final PermissionEvaluatorService permissionEvaluatorService) {
        super(dtoService, "topic", permissionEvaluatorService);
    }

    @GetMapping(produces = {"application/json"})
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, this.authorizationPrefix, 'all' )")
    public ResponseEntity<List<TopicModelDto>> getAllFromCategory(@RequestParam final String sectionCode) {
        final List<TopicModelDto> dtos = this.dtoService.getTopicsBySection(sectionCode);
        addLinks(dtos);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @Override
    public void addLinks(final TopicModelDto dto) {
        dto.add(linkTo(methodOn(TopicController.class).getSingle(dto.getCode())).withSelfRel());
        dto.add(linkTo(methodOn(TopicController.class).delete(dto.getCode())).withRel(DELETE_RELATION_NAME));
        dto.add(linkTo(methodOn(SectionController.class).delete(dto.getCode())).withRel(SECTION_RELATION_NAME));
    }
}
