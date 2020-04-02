package pl.polsl.photoplus.services.controllers;

import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.dto.CategoryModelDto;
import pl.polsl.photoplus.model.entities.Category;
import pl.polsl.photoplus.repositories.CategoryRepository;

@Service
public class CategoryService extends AbstractModelService<Category, CategoryModelDto, CategoryRepository> {

    public CategoryService(final CategoryRepository entityRepository) {
        super(entityRepository);
    }

    @Override
    protected String getModelNameForError() {
        return "category";
    }

    @Override
    protected CategoryModelDto getDtoFromModel(final Category modelObject) {
        return new CategoryModelDto(modelObject.getCode(), modelObject.getName());
    }

    @Override
    protected Category getModelFromDto(final CategoryModelDto dtoObject) {
        return new Category(dtoObject.getName());
    }
}