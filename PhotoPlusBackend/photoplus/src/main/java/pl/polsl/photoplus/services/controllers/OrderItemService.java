package pl.polsl.photoplus.services.controllers;

import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.dto.OrderItemModelDto;
import pl.polsl.photoplus.model.entities.OrderItem;
import pl.polsl.photoplus.repositories.OrderItemRepository;

@Service
public class OrderItemService extends AbstractModelService<OrderItem, OrderItemModelDto, OrderItemRepository> {
    public OrderItemService(final OrderItemRepository entityRepository) {
        super(entityRepository);
    }

    @Override
    protected String getModelNameForError() {
        return "orderItem";
    }

    @Override
    protected OrderItemModelDto getDtoFromModel(final OrderItem modelObject) {
        return new OrderItemModelDto(modelObject.getCode(), modelObject.getProduct().getCode(),
                modelObject.getOrder().getCode(), modelObject.getQuantity());
    }

    @Override
    protected OrderItem getModelFromDto(final OrderItemModelDto dtoObject) {
        return new OrderItem(dtoObject.getQuantity());
    }
}
