package pl.polsl.photoplus.controllers.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.polsl.photoplus.model.dto.TopicModelDto;
import pl.polsl.photoplus.services.controllers.PostService;
import pl.polsl.photoplus.security.services.PermissionEvaluatorService;
import pl.polsl.photoplus.services.controllers.TopicService;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/topic")
public class TopicController extends BaseModelController<TopicModelDto,TopicService> {

    private final String SECTION_RELATION_NAME = "section";
    private final PostService postService;

    public TopicController(final TopicService dtoService, final PermissionEvaluatorService permissionEvaluatorService, final PostService postService){
        super(dtoService, "topic", permissionEvaluatorService);
        this.postService = postService;
    }

    @GetMapping(path = "/bySection/{sectionCode}",produces = {"application/json"})
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, this.authorizationPrefix, 'all' )")
    public ResponseEntity<List<TopicModelDto>> getAllFromCategory(@PathVariable("sectionCode") final String sectionCode) {
        final List<TopicModelDto> dtos = this.dtoService.getTopicsBySection(sectionCode);
        addLinks(dtos);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @DeleteMapping("/deleteOwn/{code}")
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, this.getService().getTopicCreatorCode(#code))")
    public ResponseEntity deleteOwn(@PathVariable("code") final String code)
    {
        return new ResponseEntity(dtoService.delete(code));
    }


    @Override
    public void addLinks(final TopicModelDto dto) {
        dto.add(linkTo(methodOn(TopicController.class).getSingle(dto.getCode())).withSelfRel());
        dto.add(linkTo(methodOn(TopicController.class).delete(dto.getCode())).withRel(DELETE_RELATION_NAME));
        dto.add(linkTo(methodOn(SectionController.class).delete(dto.getCode())).withRel(SECTION_RELATION_NAME));
    }


    /**
     *
     * You need to use getter to use service in @PreAuthorize because SpEL cannot use inherited field.
     */
    public TopicService getService() {
        return this.dtoService;
    }
}
