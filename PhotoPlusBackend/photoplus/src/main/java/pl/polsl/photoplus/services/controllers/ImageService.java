package pl.polsl.photoplus.services.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.dto.ImageModelDto;
import pl.polsl.photoplus.model.entities.Image;
import pl.polsl.photoplus.model.entities.Product;
import pl.polsl.photoplus.repositories.ImageRepository;
import pl.polsl.photoplus.repositories.ProductRepository;

import java.util.Collections;
import java.util.List;

@Service
public class ImageService extends AbstractModelService<Image,ImageModelDto, ImageRepository>
        implements FieldValueExists {

    final ProductRepository productRepository;

    public ImageService(final ImageRepository repository, final ProductRepository productRepository) {
        super(repository);
        this.productRepository = productRepository;
    }

    @Override
    public HttpStatus delete(final String code)
    {
        final Image image = findByCodeOrThrowError(code, "DELETE");
        final List<Product> products = image.getProducts();
        if (products != null && !products.isEmpty()) {
            
            for (final Product product : products) {
                product.getImages().remove(image);
                productRepository.save(product);
            }
            image.setProducts(Collections.emptyList());
            entityRepository.save(image);
        }
        entityRepository.delete(image);
        return HttpStatus.NO_CONTENT;
    }


    @Override
    protected String getModelNameForError() {
        return "image";
    }

    @Override
    protected ImageModelDto getDtoFromModel(final Image modelObject) {
        return new ImageModelDto(modelObject.getCode(), modelObject.getName(), modelObject.getBytes());
    }

    @Override
    protected Image getModelFromDto(final ImageModelDto dtoObject) {
        return new Image(dtoObject.getName(), dtoObject.getBytes());
    }

    @Override
    public boolean fieldValueExists(final String value, final String fieldName) {
        if (fieldName.equals("name")) {
            return this.entityRepository.findByName(value).isPresent();
        }
        return false;
    }

}
