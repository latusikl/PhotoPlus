package pl.polsl.photoplus.services.controllers;

import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.dto.RatingModelDto;
import pl.polsl.photoplus.model.entities.Rating;
import pl.polsl.photoplus.repositories.RatingRepository;

@Service
public class RatingService extends AbstractModelService<Rating, RatingModelDto, RatingRepository> {
    public RatingService(final RatingRepository entityRepository) {
        super(entityRepository);
    }

    @Override
    protected String getModelNameForError() {
        return "rating";
    }

    @Override
    protected RatingModelDto getDtoFromModel(final Rating modelObject) {
        return new RatingModelDto(modelObject.getCode(), modelObject.getUser().getCode(),
                modelObject.getProduct().getCode(), modelObject.getRate(), modelObject.getContent());
    }

    @Override
    protected Rating getModelFromDto(final RatingModelDto dtoObject) {
        return new Rating(dtoObject.getRate(), dtoObject.getContent());
    }
}
