package pl.polsl.photoplus.services.controllers;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.dto.ImageModelDto;
import pl.polsl.photoplus.model.entities.Image;
import pl.polsl.photoplus.model.entities.Product;
import pl.polsl.photoplus.repositories.ImageRepository;
import pl.polsl.photoplus.repositories.ProductRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ImageService extends AbstractModelService<Image,ImageModelDto, ImageRepository> {

    final ProductService productService;
    final ProductRepository productRepository;

    public ImageService(final ImageRepository repository, @Lazy final ProductService productService, final ProductRepository productRepository) {
        super(repository);
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @Override
    protected String getModelNameForError() {
        return "image";
    }

    @Override
    protected ImageModelDto getDtoFromModel(final Image modelObject) {
        return new ImageModelDto(modelObject.getCode(), modelObject.getName(), modelObject.getBytes(),
                modelObject.getProduct() !=null ? modelObject.getProduct().getCode() : null);
    }

    @Override
    protected Image getModelFromDto(final ImageModelDto dtoObject) {
        return new Image(dtoObject.getName(), dtoObject.getBytes());
    }

    @Override
    public String save(final ImageModelDto dto) {
        return entityRepository.save(insertDependenciesAndParseToModel(dto)).getCode();
    }

    @Override
    @Transactional
    public HttpStatus delete(final String code) {
        final Image image = findByCodeOrThrowError(code, "IMAGE DELETE");
        final Product product = image.getProduct();
        if (product != null ) {
            final List<Image> imageList = product.getImages();
            if (imageList != null) {
                imageList.remove(image);
                product.setImages(imageList);
                productRepository.save(product);
                image.setProduct(null);
                entityRepository.save(image);
            }
        }
        entityRepository.delete(image);
        return HttpStatus.NO_CONTENT;
    }

    private Image insertDependenciesAndParseToModel(final ImageModelDto dto) {
        if (dto.getProduct() != null) {
            final Product productToAdd  = productService.findByCodeOrThrowError(dto.getProduct(), "SAVE IMAGE");
            final Image imageToAdd = getModelFromDto(dto);
            imageToAdd.setProduct(productToAdd);
            return imageToAdd;
        }
        return getModelFromDto(dto);
    }

}
