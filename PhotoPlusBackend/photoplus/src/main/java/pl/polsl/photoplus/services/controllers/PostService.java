package pl.polsl.photoplus.services.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.dto.PostModelDto;
import pl.polsl.photoplus.model.entities.Post;
import pl.polsl.photoplus.model.entities.Topic;
import pl.polsl.photoplus.model.entities.User;
import pl.polsl.photoplus.repositories.PostRepository;

import java.util.List;
import java.util.function.Function;

@Service
public class PostService extends AbstractModelService<Post, PostModelDto, PostRepository> {

    private final TopicService topicService;
    private final UserService userService;

    public PostService(final PostRepository entityRepository, final TopicService topicService, final UserService userService) {
        super(entityRepository);
        this.topicService = topicService;
        this.userService = userService;
    }

    @Override
    protected String getModelNameForError() {
        return "post";
    }

    @Override
    protected PostModelDto getDtoFromModel(final Post modelObject) {
        return new PostModelDto(modelObject.getCode(), modelObject.getDate(), modelObject.getTopic().getCode(),
                modelObject.getContent(), modelObject.getUser().getCode());
    }

    @Override
    protected Post getModelFromDto(final PostModelDto dtoObject) {
        return new Post(dtoObject.getDate(), dtoObject.getContent());
    }

    @Override
    public HttpStatus save(final List<PostModelDto> dto) {
        final Function<PostModelDto, Post> insertTopicDependencyAndParseToModel = postModelDto -> {
            final Topic topicToInsert = topicService.findByCodeOrThrowError(postModelDto.getTopicCode(),
                    "SAVE TOPIC");

            final User userToInsert = userService.findByCodeOrThrowError(postModelDto.getUserCode(),
                    "SAVE USER");
            final Post postToAdd = getModelFromDto(postModelDto);
            postToAdd.setTopic(topicToInsert);
            postToAdd.setUser(userToInsert);
            return postToAdd;
        };

        dto.stream().map(insertTopicDependencyAndParseToModel).forEach(entityRepository::save);
        return HttpStatus.CREATED;
    }

    public List<PostModelDto> getPostsByTopic(final String topicCode) {
        return getDtoListFromModels(this.entityRepository.findAllByTopic_Code(topicCode));
    }

    public String getPostOwnerCode(final String postCode) {
        final Post post = findByCodeOrThrowError(postCode, "Find post owner");
        return post.getUser().getCode();
    }
}
