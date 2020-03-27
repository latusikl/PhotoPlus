package pl.polsl.photoplus.services.controllers;

import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.dto.OrderModelDto;
import pl.polsl.photoplus.model.entities.Order;
import pl.polsl.photoplus.repositories.OrderRepository;

@Service
public class OrderService extends AbstractModelService<Order, OrderModelDto, OrderRepository> {
    public OrderService(final OrderRepository entityRepository) {
        super(entityRepository);
    }

    @Override
    protected String getModelNameForError() {
        return "order";
    }

    @Override
    protected OrderModelDto getDtoFromModel(final Order modelObject) {
        return new OrderModelDto(modelObject.getCode(), modelObject.getUser().getCode(), modelObject.getOrderStatus(),
                modelObject.getPaymentMethod(), modelObject.getPrice());
    }

    @Override
    protected Order getModelFromDto(final OrderModelDto dtoObject) {
        return new Order(dtoObject.getOrderStatus(), dtoObject.getPaymentMethod(), dtoObject.getPrice());
    }
}
