package pl.polsl.photoplus.services.controllers;

import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.dto.TopicModelDto;
import pl.polsl.photoplus.model.entities.Topic;
import pl.polsl.photoplus.repositories.TopicRepository;

@Service
public class TopicService extends AbstractModelService<Topic, TopicModelDto, TopicRepository> {
    public TopicService(final TopicRepository entityRepository) {
        super(entityRepository);
    }

    @Override
    protected String getModelNameForError() {
        return "topic";
    }

    @Override
    protected TopicModelDto getDtoFromModel(final Topic modelObject) {
        return new TopicModelDto(modelObject.getCode(), modelObject.getSection().getCode(), modelObject.getDate(),
                modelObject.getCreator().getCode());
    }

    @Override
    protected Topic getModelFromDto(final TopicModelDto dtoObject) {
        return new Topic(dtoObject.getName(), dtoObject.getDate());
    }
}
