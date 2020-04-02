package pl.polsl.photoplus.services.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.dto.PostModelDto;
import pl.polsl.photoplus.model.entities.Post;
import pl.polsl.photoplus.model.entities.Topic;
import pl.polsl.photoplus.repositories.PostRepository;

import java.util.Set;
import java.util.function.Function;

@Service
public class PostService extends AbstractModelService<Post, PostModelDto, PostRepository> {

    private final TopicService topicService;

    public PostService(final PostRepository entityRepository, final TopicService topicService) {
        super(entityRepository);
        this.topicService = topicService;
    }

    @Override
    protected String getModelNameForError() {
        return "post";
    }

    @Override
    protected PostModelDto getDtoFromModel(final Post modelObject) {
        return new PostModelDto(modelObject.getCode(), modelObject.getDate(), modelObject.getTopic().getCode(),
                modelObject.getContent());
    }

    @Override
    protected Post getModelFromDto(final PostModelDto dtoObject) {
        return new Post(dtoObject.getDate(), dtoObject.getContent());
    }

    @Override
    public HttpStatus save(final Set<PostModelDto> dto) {
        final Function<PostModelDto, Post> insertTopicDependencyAndParseToModel = postModelDto -> {
            final Topic topicToInsert = topicService.findByCodeOrThrowError(postModelDto.getTopicCode(),
                    "SAVE TOPIC");
            final Post postToAdd = getModelFromDto(postModelDto);
            postToAdd.setTopic(topicToInsert);
            return postToAdd;
        };

        dto.stream().map(insertTopicDependencyAndParseToModel).forEach(entityRepository::save);
        return HttpStatus.CREATED;
    }
}
