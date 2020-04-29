package pl.polsl.photoplus.controllers.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.photoplus.model.dto.PostModelDto;
import pl.polsl.photoplus.services.controllers.PostService;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequestMapping("/post")
@RestController
public class PostController extends BaseModelController<PostModelDto,PostService> {

    private final String TOPIC_RELATION_NAME = "topic";
    private final String CREATOR_RELATION_NAME  = "creator";

    public PostController(final PostService dtoService) {
        super(dtoService, "post");
    }

    @GetMapping(produces = {"application/hal+json"})
    @PreAuthorize("hasPermission(this.authorizationPrefix, 'all' )")
    public ResponseEntity<List<PostModelDto>> getAllFromCategory(@RequestParam final String topicCode) {
        final List<PostModelDto> dtos = this.dtoService.getPostsByTopic(topicCode);
        addLinks(dtos);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @Override
    public void addLinks(final PostModelDto dto) {
        dto.add(linkTo(methodOn(PostController.class).getSingle(dto.getCode())).withSelfRel());
        dto.add(linkTo(methodOn(PostController.class).delete(dto.getCode())).withRel(DELETE_RELATION_NAME));
        dto.add(linkTo(methodOn(TopicController.class).getSingle(dto.getTopicCode())).withRel(TOPIC_RELATION_NAME));
        dto.add(linkTo(methodOn(UserController.class).getSingle(dto.getUserCode())).withRel(CREATOR_RELATION_NAME));
    }
}
