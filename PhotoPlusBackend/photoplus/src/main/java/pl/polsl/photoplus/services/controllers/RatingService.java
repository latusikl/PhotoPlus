package pl.polsl.photoplus.services.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
                modelObject.getProduct().getCode(), modelObject.getRate(), modelObject.getContent(),
                modelObject.getUser().getLogin(), modelObject.getDate());
    }

    @Override
    protected Rating getModelFromDto(final RatingModelDto dtoObject) {
        return new Rating(dtoObject.getRate(), dtoObject.getContent(), dtoObject.getDate());
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

    public List<RatingModelDto> getRatingsByProductCode(final String code) {
        return getDtoListFromModels(entityRepository.getAllByProduct_Code(code));
    }

    public List<RatingModelDto> getPageByProductCode(final Integer page, final String sortedBy, final String productCode) {
        return getDtoListFromModels(getPageOfRatingByProductCode(page, sortedBy, productCode));
    }

    private Page<Rating> getPageOfRatingByProductCode(final Integer pageNumber, final String sortedBy, final String productCode) {
        final Pageable modelPage;
        if (sortedBy.equals("rateAsc")) {
            modelPage = PageRequest.of(pageNumber, modelPropertiesService.getPageSize(), Sort.by("rate"));
        } else if (sortedBy.equals("rateDesc")) {
            modelPage = PageRequest.of(pageNumber, modelPropertiesService.getPageSize(), Sort.by("rate").descending());
        } else if (sortedBy.equals("dateAsc")) {
            modelPage = PageRequest.of(pageNumber, modelPropertiesService.getPageSize(), Sort.by("date"));
        } else {
            modelPage = PageRequest.of(pageNumber, modelPropertiesService.getPageSize(), Sort.by("date").descending());
        }
        final Page<Rating> foundModels = entityRepository.findAllByProduct_Code(modelPage, productCode);
        return foundModels;
    }

    public ObjectNode getPageCountOfRatingByProductCode(final String productCode)
    {
        final Page<Rating> firstPage = getPageOfRatingByProductCode(0, "dateDesc", productCode);
        final ObjectNode jsonNode = objectMapper.createObjectNode();

        jsonNode.put("pageAmount", firstPage.getTotalPages());
        jsonNode.put("pageSize", modelPropertiesService.getPageSize());

        return jsonNode;
    }
}
