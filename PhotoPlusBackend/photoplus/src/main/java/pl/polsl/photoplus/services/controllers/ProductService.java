package pl.polsl.photoplus.services.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import pl.polsl.photoplus.model.dto.ProductModelDto;
import pl.polsl.photoplus.model.entities.Category;
import pl.polsl.photoplus.model.entities.Product;
import pl.polsl.photoplus.repositories.ProductRepository;
import pl.polsl.photoplus.services.controllers.exceptions.NotEnoughProductsException;

import java.util.List;
import java.util.function.Function;

@Service
public class ProductService extends AbstractModelService<Product, ProductModelDto, ProductRepository> {

    private final CategoryService categoryService;

    public ProductService(final ProductRepository entityRepository, final CategoryService categoryService) {
        super(entityRepository);
        this.categoryService = categoryService;
    }

    @Override
    protected String getModelNameForError() {
        return "product";
    }

    @Override
    protected ProductModelDto getDtoFromModel(final Product modelObject) {
        return new ProductModelDto(modelObject.getCode(), modelObject.getName(), modelObject.getPrice(),
                modelObject.getDescription(), modelObject.getCategory().getCode(), modelObject.getStoreQuantity(),
                modelObject.getImageCodes());
    }

    @Override
    protected Product getModelFromDto(final ProductModelDto dtoObject) {
        return new Product(dtoObject.getName(), dtoObject.getPrice(), dtoObject.getDescription(), dtoObject.getStoreQuantity(),
                dtoObject.getImageCodes());
    }

    @Override
    public HttpStatus save(final List<ProductModelDto> dtoSet) {
        final Function<ProductModelDto, Product> insertCategoryDependencyAndParseToModel = productModelDto -> {
            final Category categoryToAdd = categoryService.findByCodeOrThrowError(productModelDto.getCategory(),
                    "SAVE PRODUCT");
            final Product productToAdd = getModelFromDto(productModelDto);
            productToAdd.setCategory(categoryToAdd);
            return productToAdd;
        };

        dtoSet.stream().map(insertCategoryDependencyAndParseToModel).forEach(this.entityRepository::save);
        return HttpStatus.CREATED;
    }

    public List<ProductModelDto> getProductsFromCategory(final String categoryCode)
    {
        return getDtoListFromModels(this.entityRepository.getAllByCategory_Code(categoryCode));
    }

    public void updateStoreQuantity(final String productCode, final Integer quantityToSub) {
        final Product product = this.findByCodeOrThrowError(productCode, "UPDATE STORE QUANTITY");
        final Integer newQuantity = product.getStoreQuantity() - quantityToSub;
        if (newQuantity < 0) {
            throw new NotEnoughProductsException("Not enough " + product.getName() +" in store.", product.getName());
        }
        product.setStoreQuantity(newQuantity);
        this.entityRepository.save(product);
    }
}
