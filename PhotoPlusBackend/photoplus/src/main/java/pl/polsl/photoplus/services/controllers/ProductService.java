package pl.polsl.photoplus.services.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import pl.polsl.photoplus.model.dto.BatchModelDto;
import pl.polsl.photoplus.model.dto.ProductModelDto;
import pl.polsl.photoplus.model.entities.Category;
import pl.polsl.photoplus.model.entities.Image;
import pl.polsl.photoplus.model.entities.Product;
import pl.polsl.photoplus.repositories.ProductRepository;
import pl.polsl.photoplus.services.controllers.exceptions.NotEnoughProductsException;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService extends AbstractModelService<Product, ProductModelDto, ProductRepository> {

    private final CategoryService categoryService;
    private final ImageService imageService;
    private final BatchService batchService;

    public ProductService(final ProductRepository entityRepository, final CategoryService categoryService,
                          final ImageService imageService, @Lazy final BatchService batchService) {
        super(entityRepository);
        this.categoryService = categoryService;
        this.imageService = imageService;
        this.batchService = batchService;
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

    public List<ProductModelDto> getAvailableProductsPageFromAll(final Integer page, final String sortedBy) {
        return getDtoListFromModels(getAvailableProductsPage(page, sortedBy));
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
    public HttpStatus saveAll(final List<ProductModelDto> dtoSet) {
        dtoSet.stream().map(this::insertDependenciesAndParseToModel).forEach(this.entityRepository::save);
        return HttpStatus.CREATED;
    }

    public List<ProductModelDto> getAvailableProductsFromCategory(final Integer pageNumber, final String categoryCode)
    {
        return getDtoListFromModels(this.getPageOfAvailableProductsFromCategory(pageNumber, categoryCode));
    }

    private Page<Product> getPageOfAvailableProductsFromCategory(final Integer pageNumber, final String categoryCode) {
        final Pageable modelPage = PageRequest.of(pageNumber, modelPropertiesService.getPageSize(), Sort.by("name"));
        final Page<Product> foundModels = entityRepository.findAllByCategory_CodeAndStoreQuantityGreaterThan(modelPage, categoryCode, 0);
        return foundModels;
    }

    public ObjectNode getPageCountOfAvailableProductsFromCategory(final String categoryCode)
    {
        final Page<Product> firstPage = getPageOfAvailableProductsFromCategory(0, categoryCode);
        final ObjectNode jsonNode = objectMapper.createObjectNode();

        jsonNode.put("pageAmount", firstPage.getTotalPages());
        jsonNode.put("pageSize", modelPropertiesService.getPageSize());

        return jsonNode;
    }

    public ObjectNode getPageCountOfNameContainingStr(final String str)
    {
        final Page<Product> firstPage = getPageOfProductByNameContainingStr(str, 0,"name");
        final ObjectNode jsonNode = objectMapper.createObjectNode();

        jsonNode.put("pageAmount", firstPage.getTotalPages());
        jsonNode.put("pageSize", modelPropertiesService.getPageSize());

        return jsonNode;
    }

    public ObjectNode getPageCountOfAllByNameContainingStr(final String str)
    {
        final Page<Product> firstPage = getPageOfAllProductsByNameContainingStr(str, 0,"name");
        final ObjectNode jsonNode = objectMapper.createObjectNode();

        jsonNode.put("pageAmount", firstPage.getTotalPages());
        jsonNode.put("pageSize", modelPropertiesService.getPageSize());

        return jsonNode;
    }

    public ObjectNode getAvailablePageCount() {
        final Page<Product> firstPage = getAvailableProductsPage(0, "name");
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

    public List<ProductModelDto> getByNameContainingStr(final String str, final Integer pageNumber, final String sortedBy) {
        return getDtoListFromModels(this.getPageOfProductByNameContainingStr(str, pageNumber, sortedBy));
    }

    private Page<Product> getPageOfProductByNameContainingStr(final String str, final Integer pageNumber, final String sortedBy) {
        final Pageable modelPage = getModelPage(pageNumber, sortedBy);
        final Page<Product> foundModels = entityRepository.findAllByNameContainingIgnoreCaseAndStoreQuantityGreaterThan(modelPage, str, 0);
        return foundModels;
    }

    public List<ProductModelDto> getAllByNameContainingStr(final String str, final Integer pageNumber, final String sortedBy) {
        return getDtoListFromModels(this.getPageOfAllProductsByNameContainingStr(str, pageNumber, sortedBy));
    }

    private Page<Product> getPageOfAllProductsByNameContainingStr(final String str, final Integer pageNumber, final String sortedBy) {
        final Pageable modelPage = getModelPage(pageNumber, sortedBy);
        final Page<Product> foundModels = entityRepository.findAllByNameContainingIgnoreCase(modelPage, str);
        return foundModels;
    }

    private Pageable getModelPage(final Integer pageNumber, final String sortedBy) {
        if (sortedBy.equals("priceAsc")) {
            return PageRequest.of(pageNumber, modelPropertiesService.getPageSize(), Sort.by("price"));
        } else if (sortedBy.equals("priceDesc")) {
            return PageRequest.of(pageNumber, modelPropertiesService.getPageSize(), Sort.by("price").descending());
        }
        return PageRequest.of(pageNumber, modelPropertiesService.getPageSize(), Sort.by("name"));
    }


    private Page<Product> getAvailableProductsPage(final Integer pageNumber, final String sortedBy) {
        final Pageable modelPage = getModelPage(pageNumber, sortedBy);
        final Page<Product> foundModels = entityRepository.findAllByStoreQuantityGreaterThan(modelPage, 0);
        throwNotFoundErrorIfIterableEmpty("FIND ALL", foundModels);
        return foundModels;
    }

    public List<ProductModelDto> getTopEight() {
        return getDtoListFromModels(this.entityRepository.findTop8ByStoreQuantityGreaterThan(0));
    }

    public Double getAveragePurchasePrice(final String productCode) {
        final List<BatchModelDto> batchList = batchService.getAllByProduct(productCode);
        if (batchList.isEmpty()) {
            return 0.0;
        }

        Double price = 0.0;
        Integer supplyQuantity = 0;
        for (final BatchModelDto batch : batchList) {
            price += batch.getPurchasePrice() * batch.getSupplyQuantity();
            supplyQuantity += batch.getSupplyQuantity();
        }

        if (supplyQuantity == 0) {
            return 0.0;
        }

        return price / supplyQuantity;
    }
}
