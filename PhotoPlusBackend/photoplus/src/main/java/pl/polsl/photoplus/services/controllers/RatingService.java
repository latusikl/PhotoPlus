package pl.polsl.photoplus.services.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.dto.RatingModelDto;
import pl.polsl.photoplus.model.entities.Product;
import pl.polsl.photoplus.model.entities.Rating;
import pl.polsl.photoplus.model.entities.User;
import pl.polsl.photoplus.repositories.RatingRepository;

import java.util.List;

@Service
public class RatingService extends AbstractModelService<Rating, RatingModelDto, RatingRepository> {

    private UserService userService;

    private ProductService productService;

    public RatingService(final RatingRepository entityRepository, final UserService userService, final ProductService productService) {
        super(entityRepository);
        this.userService = userService;
        this.productService = productService;
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

    private Rating insertDependenciesAndParseToModel(final RatingModelDto dto) {
        final User userToInsert = userService.findByCodeOrThrowError(dto.getUserCode(),
                "SAVE RATING");
        final Product productToInsert = productService.findByCodeOrThrowError(dto.getProductCode(),
                "SAVE RATING");
        final Rating ratingToAdd = getModelFromDto(dto);
        ratingToAdd.setUser(userToInsert);
        ratingToAdd.setProduct(productToInsert);
        return ratingToAdd;
    }

    @Override
    public String save(final RatingModelDto dto) {
        final String entityCode = entityRepository.save(insertDependenciesAndParseToModel(dto)).getCode();
        return entityCode;
    }

    @Override
    public HttpStatus saveAll(final List<RatingModelDto> dto) {
        dto.stream().map(this::insertDependenciesAndParseToModel).forEach(entityRepository::save);
        return HttpStatus.CREATED;
    }
}
