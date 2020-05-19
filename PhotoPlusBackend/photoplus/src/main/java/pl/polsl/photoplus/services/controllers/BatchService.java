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

    private Batch insertProductDependencyAndParseToModel(final BatchModelDto dto) {
        productService.addStoreQuantity(dto.getProductCode(), dto.getStoreQuantity());
        final Product productToInsert = productService.findByCodeOrThrowError(dto.getProductCode(),
                "SAVE PRODUCT");
        final Batch batchToAdd = getModelFromDto(dto);
        batchToAdd.setProduct(productToInsert);
        return batchToAdd;
    }

    @Override
    @Transactional
    public String save(final BatchModelDto dto) {
        final String entityCode = entityRepository.save(insertProductDependencyAndParseToModel(dto)).getCode();
        return entityCode;
    }

    @Override
    @Transactional
    public HttpStatus saveAll(final List<BatchModelDto> dto) {
        dto.stream().map(this::insertProductDependencyAndParseToModel).forEach(entityRepository::save);
        return HttpStatus.CREATED;
    }

    public List<BatchModelDto> getAllByProduct(final String productCode) {
        return getDtoListFromModels(this.entityRepository.getAllByProduct_CodeOrderByDate(productCode));
    }

    public void subStoreQuantity(final String productCode, Integer quantityToSub) {
        final List<Batch> batchList = this.entityRepository.getAllByProduct_CodeOrderByDate(productCode);
        final int quantitySum = batchList.stream().mapToInt(Batch::getStoreQuantity).sum();

        if (quantitySum - quantityToSub < 0) {
            final Product product = productService.findByCodeOrThrowError(productCode, "SUB STORE QUANTITY");
            throw new NotEnoughProductsException("Not enough " + product.getName() +" in store.", product.getName());
        }

        for (final var batch: batchList) {
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
