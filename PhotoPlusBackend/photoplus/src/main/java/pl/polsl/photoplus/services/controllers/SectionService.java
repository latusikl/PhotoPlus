package pl.polsl.photoplus.services.controllers;

import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.dto.SectionModelDto;
import pl.polsl.photoplus.model.entities.Section;
import pl.polsl.photoplus.repositories.SectionRepository;

import java.util.List;

@Service
public class SectionService extends AbstractModelService<Section, SectionModelDto, SectionRepository> {

    public SectionService(final SectionRepository entityRepository) {
        super(entityRepository);
    }

    @Override
    public List<SectionModelDto> getAll() {
        return getDtoListFromModels(entityRepository.findAllByOrderByName());
    }

    @Override
    protected String getModelNameForError() {
        return "Section";
    }

    @Override
    protected SectionModelDto getDtoFromModel(final Section modelObject) {
        return new SectionModelDto(modelObject.getCode(), modelObject.getName(), modelObject.getDescription());
    }

    @Override
    protected Section getModelFromDto(final SectionModelDto dtoObject) {
        return new Section(dtoObject.getName(), dtoObject.getDescription());
    }
}
