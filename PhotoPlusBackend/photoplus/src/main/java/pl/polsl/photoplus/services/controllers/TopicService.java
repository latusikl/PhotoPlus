package pl.polsl.photoplus.services.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.dto.TopicModelDto;
import pl.polsl.photoplus.model.entities.*;
import pl.polsl.photoplus.repositories.TopicRepository;

import java.util.List;
import java.util.function.Function;

@Service
public class TopicService extends AbstractModelService<Topic, TopicModelDto, TopicRepository> {

    private UserService userService;

    private SectionService sectionService;

    public TopicService(final TopicRepository entityRepository, final UserService userService, final SectionService sectionService) {
        super(entityRepository);
        this.userService = userService;
        this.sectionService = sectionService;
    }

    @Override
    protected String getModelNameForError() {
        return "topic";
    }

    @Override
    protected TopicModelDto getDtoFromModel(final Topic modelObject) {
        return new TopicModelDto(modelObject.getCode(), modelObject.getName(), modelObject.getSection().getCode(), modelObject.getDate(),
                modelObject.getCreator().getCode());
    }

    @Override
    protected Topic getModelFromDto(final TopicModelDto dtoObject) {
        return new Topic(dtoObject.getName(), dtoObject.getDate());
    }

    @Override
    public HttpStatus save(final List<TopicModelDto> dto) {
        final Function<TopicModelDto, Topic> insertDependenciesAndParseToModel = topicModelDto -> {
            final User userToInsert = userService.findByCodeOrThrowError(topicModelDto.getUserCode(),
                    "SAVE USER");
            final Section orderToInsert = sectionService.findByCodeOrThrowError(topicModelDto.getSectionCode(),
                    "SAVE SECTION");
            final Topic topicToAdd = getModelFromDto(topicModelDto);
            topicToAdd.setCreator(userToInsert);
            topicToAdd.setSection(orderToInsert);
            return topicToAdd;
        };

        dto.stream().map(insertDependenciesAndParseToModel).forEach(entityRepository::save);
        return HttpStatus.CREATED;
    }
}
