package pl.polsl.photoplus.controllers.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.photoplus.model.dto.PostModelDto;
import pl.polsl.photoplus.services.controllers.PostService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequestMapping("/post")
@RestController
public class PostController extends BaseModelController<PostModelDto> {

    private final String TOPIC_RELATION_NAME = "topic";

    public PostController(final PostService dtoService) {
        super(dtoService, "/post");
    }

    @Override
    public void addLinks(final PostModelDto dto) {
        dto.add(linkTo(methodOn(PostController.class).getSingle(dto.getCode())).withSelfRel());
        dto.add(linkTo(methodOn(PostController.class).delete(dto.getCode())).withRel(DELETE_RELATION_NAME));
        dto.add(linkTo(methodOn(TopicController.class).getSingle(dto.getTopicCode())).withRel(TOPIC_RELATION_NAME));
    }
}
