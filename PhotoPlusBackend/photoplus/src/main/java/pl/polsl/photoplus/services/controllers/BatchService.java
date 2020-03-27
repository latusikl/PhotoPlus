package pl.polsl.photoplus.services.controllers;

import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.dto.BatchModelDto;
import pl.polsl.photoplus.model.entities.Batch;
import pl.polsl.photoplus.repositories.BatchRepository;

@Service
public class BatchService extends AbstractModelService<Batch, BatchModelDto, BatchRepository> {
    public BatchService(final BatchRepository entityRepository) {
        super(entityRepository);
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
}
