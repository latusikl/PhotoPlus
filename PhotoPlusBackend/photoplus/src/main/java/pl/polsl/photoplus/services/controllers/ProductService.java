package pl.polsl.photoplus.services.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import pl.polsl.photoplus.model.dto.ProductModelDto;
import pl.polsl.photoplus.model.entities.Category;
import pl.polsl.photoplus.model.entities.Image;
import pl.polsl.photoplus.model.entities.Product;
import pl.polsl.photoplus.repositories.ImageRepository;
import pl.polsl.photoplus.repositories.ProductRepository;
import pl.polsl.photoplus.services.controllers.exceptions.NotEnoughProductsException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ProductService extends AbstractModelService<Product, ProductModelDto, ProductRepository> {

    private final CategoryService categoryService;
    private final ImageService imageService;
    private final ImageRepository imageRepository;

    public ProductService(final ProductRepository entityRepository, final CategoryService categoryService, final ImageService imageService, final ImageRepository imageRepository) {
        super(entityRepository);
        this.categoryService = categoryService;
        this.imageService = imageService;
        this.imageRepository = imageRepository;
    }

    @Override
    protected String getModelNameForError() {
        return "product";
    }

    @Override
    protected ProductModelDto getDtoFromModel(final Product modelObject) {
        final List<String> imageCodes = new ArrayList<>();
        modelObject.getImages().forEach(image -> imageCodes.add(image.getCode()));
        return new ProductModelDto(modelObject.getCode(), modelObject.getName(), modelObject.getPrice(),
                modelObject.getDescription(), modelObject.getCategory().getCode(), modelObject.getStoreQuantity(),
                imageCodes, modelObject.getDataLinks());
    }

    @Override
    protected Product getModelFromDto(final ProductModelDto dtoObject) {
        return new Product(dtoObject.getName(), dtoObject.getPrice(), dtoObject.getDescription(),
                dtoObject.getStoreQuantity(), dtoObject.getDataLinks());
    }

    public List<ProductModelDto> getPageFromAllSortedByName(final Integer page) {
        return getDtoListFromModels(getPageSortByName(page));
    }

    public List<ProductModelDto> getPageFromAllSortedPriceAsc(final Integer page) {
        return getDtoListFromModels(getPageSortByPriceAsc(page));
    }

    public List<ProductModelDto> getPageFromAllSortedPriceDesc(final Integer page) {
        return getDtoListFromModels(getPageSortByPriceDesc(page));
    }

    private Product insertDependenciesAndParseToModel(final ProductModelDto dto) {
        final Category categoryToAdd = categoryService.findByCodeOrThrowError(dto.getCategory(),
                "SAVE PRODUCT");
        final List<Image> imagesToAdd = new ArrayList<>();
        dto.getImages().forEach(imageCode -> {
            final Image imageToAdd = imageService.findByCodeOrThrowError(imageCode,
                    "SAVE PRODUCT");
            imagesToAdd.add(imageToAdd);
        });
        final Product productToAdd = getModelFromDto(dto);
        productToAdd.setCategory(categoryToAdd);
        productToAdd.setImages(imagesToAdd);
        return productToAdd;
    }

    @Override
    public String save(final ProductModelDto dto) {
        final String entityCode = entityRepository.save(insertDependenciesAndParseToModel(dto)).getCode();
        return entityCode;
    }

    @Override
    @Transactional
    public HttpStatus delete(final String code)
    {
        final Product product = findByCodeOrThrowError(code, "PRODUCT DELETE");
        final List<Image> images = product.getImages();
        if (images != null && !images.isEmpty()) {

            for (final Image image : images) {
                image.getProducts().remove(product);
                imageRepository.save(image);
            }
            product.setImages(Collections.emptyList());
            entityRepository.save(product);
        }
        entityRepository.delete(product);
        return HttpStatus.NO_CONTENT;
    }

    @Override
    public HttpStatus saveAll(final List<ProductModelDto> dtoSet) {
        dtoSet.stream().map(this::insertDependenciesAndParseToModel).forEach(this.entityRepository::save);
        return HttpStatus.CREATED;
    }

    public List<ProductModelDto> getProductsFromCategory(final Integer pageNumber, final String categoryCode)
    {
        return getDtoListFromModels(this.getPageOfProductFromCategory(pageNumber, categoryCode));
    }

    private Page<Product> getPageOfProductFromCategory(final Integer pageNumber, final String categoryCode) {
        final Pageable modelPage = PageRequest.of(pageNumber, modelPropertiesService.getPageSize());
        final Page<Product> foundModels = entityRepository.findAllByCategory_CodeOrderByName(modelPage, categoryCode);
        throwNotFoundErrorIfIterableEmpty("FIND ALL", foundModels);
        return foundModels;
    }

    public ObjectNode getPageCountOfProductFromCategory(final String categoryCode)
    {
        final Page<Product> firstPage = getPageOfProductFromCategory(0, categoryCode);
        final ObjectNode jsonNode = objectMapper.createObjectNode();

        jsonNode.put("pageAmount", firstPage.getTotalPages());
        jsonNode.put("pageSize", modelPropertiesService.getPageSize());

        return jsonNode;
    }

    public void subStoreQuantity(final String productCode, final Integer quantityToSub) {
        final Product product = this.findByCodeOrThrowError(productCode, "SUB STORE QUANTITY");
        final Integer newQuantity = product.getStoreQuantity() - quantityToSub;
        if (newQuantity < 0) {
            throw new NotEnoughProductsException("Not enough " + product.getName() +" in store.", product.getName());
        }
        product.setStoreQuantity(newQuantity);
        this.entityRepository.save(product);
    }

    public void addStoreQuantity(final String productCode, final Integer quantityToAdd) {
        final Product product = this.findByCodeOrThrowError(productCode, "ADD STORE QUANTITY");
        final Integer newQuantity = product.getStoreQuantity() + quantityToAdd;
        product.setStoreQuantity(newQuantity);
        this.entityRepository.save(product);
    }

    public List<ProductModelDto> getByNameContainingStr(final String str) {
        return getDtoListFromModels(entityRepository.findAllByNameContainingIgnoreCaseOrderByName(str));
    }

    private Page<Product> getPageSortByName(final Integer pageNumber) {
        final Pageable modelPage = PageRequest.of(pageNumber, modelPropertiesService.getPageSize());
        final Page<Product> foundModels = entityRepository.findAllByOrderByName(modelPage);
        throwNotFoundErrorIfIterableEmpty("FIND ALL", foundModels);
        return foundModels;
    }

    private Page<Product> getPageSortByPriceAsc(final Integer pageNumber) {
        final Pageable modelPage = PageRequest.of(pageNumber, modelPropertiesService.getPageSize());
        final Page<Product> foundModels = entityRepository.findAllByOrderByPriceAsc(modelPage);
        throwNotFoundErrorIfIterableEmpty("FIND ALL", foundModels);
        return foundModels;
    }

    private Page<Product> getPageSortByPriceDesc(final Integer pageNumber) {
        final Pageable modelPage = PageRequest.of(pageNumber, modelPropertiesService.getPageSize());
        final Page<Product> foundModels = entityRepository.findAllByOrderByPriceDesc(modelPage);
        throwNotFoundErrorIfIterableEmpty("FIND ALL", foundModels);
        return foundModels;
    }

    public List<ProductModelDto> getTopEight() {
        return getDtoListFromModels(this.entityRepository.findTop8ByStoreQuantityGreaterThan(0));
    }
}
