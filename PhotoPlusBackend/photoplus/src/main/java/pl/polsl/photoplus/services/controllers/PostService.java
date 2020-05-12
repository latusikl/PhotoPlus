package pl.polsl.photoplus.services.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.dto.PostModelDto;
import pl.polsl.photoplus.model.entities.Post;
import pl.polsl.photoplus.model.entities.Topic;
import pl.polsl.photoplus.model.entities.User;
import pl.polsl.photoplus.repositories.PostRepository;

import java.util.List;

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
                modelObject.getContent(), modelObject.getUser().getCode(), modelObject.getUser().getLogin());
    }

    @Override
    protected Post getModelFromDto(final PostModelDto dtoObject) {
        return new Post(dtoObject.getDate(), dtoObject.getContent());
    }

    private Post insertTopicDependencyAndParseToModel(final PostModelDto dto) {
        final Topic topicToInsert = topicService.findByCodeOrThrowError(dto.getTopicCode(),
                "SAVE TOPIC");

        final User userToInsert = userService.findByCodeOrThrowError(dto.getUserCode(),
                "SAVE USER");
        final Post postToAdd = getModelFromDto(dto);
        postToAdd.setTopic(topicToInsert);
        postToAdd.setUser(userToInsert);
        return postToAdd;
    }

    @Override
    public String save(final PostModelDto dto) {
        entityRepository.save(insertTopicDependencyAndParseToModel(dto));
        return "dd";
    }

    @Override
    public HttpStatus saveAll(final List<PostModelDto> dto) {
        dto.stream().map(this::insertTopicDependencyAndParseToModel).forEach(entityRepository::save);
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
