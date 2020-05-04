package pl.polsl.photoplus.controllers.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.polsl.photoplus.model.dto.PostModelDto;
import pl.polsl.photoplus.model.dto.TopicModelDto;
import pl.polsl.photoplus.services.controllers.PostService;
import pl.polsl.photoplus.security.services.PermissionEvaluatorService;
import pl.polsl.photoplus.services.controllers.TopicService;

import javax.validation.Valid;
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

    /*
    @PostMapping
    @PreAuthorize("hasPermission(this.authorizationPrefix, 'post' )")
    public ResponseEntity<List<String>> post(@RequestBody @Valid final List<TopicModelDto> dtos){
        final List<String> codesFromSavedDtos = this.dtoService.saveAndReturnSaved(dtos);
        return new ResponseEntity(codesFromSavedDtos, HttpStatus.OK);
    }*/

    @DeleteMapping(path = "/delete/{code}")
    @PreAuthorize("hasPermission(this.authorizationPrefix, 'delete' )")
    @Override
    public ResponseEntity delete(@PathVariable("code") final String code)
    {
        final List<PostModelDto> postsByTopic = postService.getPostsByTopic(code);
        for(final var post : postsByTopic){
            postService.delete(post.getCode());
        }
        return new ResponseEntity(dtoService.delete(code));
    }

    @Override
    public void addLinks(final TopicModelDto dto) {
        dto.add(linkTo(methodOn(TopicController.class).getSingle(dto.getCode())).withSelfRel());
        dto.add(linkTo(methodOn(TopicController.class).delete(dto.getCode())).withRel(DELETE_RELATION_NAME));
        dto.add(linkTo(methodOn(SectionController.class).delete(dto.getCode())).withRel(SECTION_RELATION_NAME));
    }
}
