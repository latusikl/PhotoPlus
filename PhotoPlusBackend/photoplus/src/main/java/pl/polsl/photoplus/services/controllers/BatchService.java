package pl.polsl.photoplus.services.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.dto.BatchModelDto;
import pl.polsl.photoplus.model.entities.Batch;
import pl.polsl.photoplus.model.entities.Product;
import pl.polsl.photoplus.repositories.BatchRepository;
import pl.polsl.photoplus.services.controllers.exceptions.NotEnoughProductsException;

import javax.transaction.Transactional;
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
    @Transactional
    public HttpStatus save(final List<BatchModelDto> dto) {
        final Function<BatchModelDto, Batch> insertProductDependencyAndParseToModel = batchModelDto -> {
            productService.addStoreQuantity(batchModelDto.getProductCode(), batchModelDto.getStoreQuantity());
            final Product productToInsert = productService.findByCodeOrThrowError(batchModelDto.getProductCode(),
                    "SAVE PRODUCT");
            final Batch batchToAdd = getModelFromDto(batchModelDto);
            batchToAdd.setProduct(productToInsert);
            return batchToAdd;
        };

        dto.stream().map(insertProductDependencyAndParseToModel).forEach(entityRepository::save);
        return HttpStatus.CREATED;
    }

    public void subStoreQuantity(final String productCode, Integer quantityToSub) {
        final List<Batch> batchList = this.entityRepository.getAllByProduct_CodeOrderByDate(productCode);
        final int quantitySum = batchList.stream().mapToInt(Batch::getStoreQuantity).sum();

        if (quantitySum - quantityToSub < 0) {
            final Product product = productService.findByCodeOrThrowError(productCode, "SUB STORE QUANTITY");
            throw new NotEnoughProductsException("Not enough " + product.getName() +" in store.", product.getName());
        }

        for (int i = 0; i < batchList.size(); i++) {
            final Batch batch = batchList.get(i);
            final Integer newQuantity = batch.getStoreQuantity() - quantityToSub;
            if (newQuantity >= 0) {
                batch.setStoreQuantity(newQuantity);
                this.entityRepository.save(batch);
                break;
            } else {
                quantityToSub -= batch.getStoreQuantity();
                batch.setStoreQuantity(0);
                this.entityRepository.save(batch);
            }
        }

    }

}
