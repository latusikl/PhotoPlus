package pl.polsl.photoplus.services.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.dto.RatingModelDto;
import pl.polsl.photoplus.model.entities.Product;
import pl.polsl.photoplus.model.entities.Rating;
import pl.polsl.photoplus.model.entities.User;
import pl.polsl.photoplus.repositories.RatingRepository;

import java.util.Set;
import java.util.function.Function;

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

    @Override
    public HttpStatus save(final Set<RatingModelDto> dto) {
        final Function<RatingModelDto, Rating> insertDependenciesAndParseToModel = ratingModelDto -> {
            final User userToInsert = userService.findByCodeOrThrowError(ratingModelDto.getUserCode(),
                    "SAVE USER");
            final Product productToInsert = productService.findByCodeOrThrowError(ratingModelDto.getProductCode(),
                    "SAVE SECTION");
            final Rating ratingToAdd = getModelFromDto(ratingModelDto);
            ratingToAdd.setUser(userToInsert);
            ratingToAdd.setProduct(productToInsert);
            return ratingToAdd;
        };

        dto.stream().map(insertDependenciesAndParseToModel).forEach(entityRepository::save);
        return HttpStatus.CREATED;
    }
}
