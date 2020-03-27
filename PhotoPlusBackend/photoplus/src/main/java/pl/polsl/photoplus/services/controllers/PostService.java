package pl.polsl.photoplus.services.controllers;

import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.dto.PostModelDto;
import pl.polsl.photoplus.model.entities.Post;
import pl.polsl.photoplus.repositories.PostRepository;

@Service
public class PostService extends AbstractModelService<Post, PostModelDto, PostRepository> {

    public PostService(final PostRepository entityRepository) {
        super(entityRepository);
    }

    @Override
    protected String getModelNameForError() {
        return null;
    }

    @Override
    protected PostModelDto getDtoFromModel(final Post modelObject) {
        return new PostModelDto(modelObject.getCode(), modelObject.getDate(), modelObject.getTopic().getCode());
    }

    @Override
    protected Post getModelFromDto(final PostModelDto dtoObject) {
        return new Post(dtoObject.getDate());
    }
}
