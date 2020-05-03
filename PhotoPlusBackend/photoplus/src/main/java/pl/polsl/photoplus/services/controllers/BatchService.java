package pl.polsl.photoplus.services.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.dto.BatchModelDto;
import pl.polsl.photoplus.model.entities.Batch;
import pl.polsl.photoplus.model.entities.Product;
import pl.polsl.photoplus.repositories.BatchRepository;

import java.util.List;
import java.util.function.Function;

@Service
public class BatchService extends AbstractModelService<Batch, BatchModelDto, BatchRepository> {

    private final ProductService productService;

    public BatchService(final BatchRepository entityRepository, final ProductService productService) {
        super(entityRepository);
        this.productService = productService;
    }

    @Override
    protected String getModelNameForError() {
        return "batch";
    }

    @Override
    protected BatchModelDto getDtoFromModel(final Batch modelObject) {
        return new BatchModelDto(modelObject.getCode(), modelObject.getProduct().getCode(),
                modelObject.getPurchasePrice(), modelObject.getDate(), modelObject.getSupplyQuantity(),
                modelObject.getStoreQuantity());
    }

    @Override
    protected Batch getModelFromDto(final BatchModelDto dtoObject) {
        return new Batch(dtoObject.getPurchasePrice(), dtoObject.getDate(), dtoObject.getSupplyQuantity(),
                dtoObject.getStoreQuantity());
    }

    @Override
    public HttpStatus save(final List<BatchModelDto> dto) {
        final Function<BatchModelDto, Batch> insertProductDependencyAndParseToModel = batchModelDto -> {
            final Product productToInsert = productService.findByCodeOrThrowError(batchModelDto.getProductCode(),
                    "SAVE PRODUCT");
            productToInsert.setStoreQuantity(productToInsert.getStoreQuantity() + batchModelDto.getStoreQuantity());
            productService.patch(productService.getDtoFromModel(productToInsert), productToInsert.getCode());
            final Batch batchToAdd = getModelFromDto(batchModelDto);
            batchToAdd.setProduct(productToInsert);
            return batchToAdd;
        };

        dto.stream().map(insertProductDependencyAndParseToModel).forEach(entityRepository::save);
        return HttpStatus.CREATED;
    }

}
