package pl.polsl.photoplus.services.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.dto.OrderModelDto;
import pl.polsl.photoplus.model.entities.Order;
import pl.polsl.photoplus.model.entities.OrderItem;
import pl.polsl.photoplus.model.entities.User;
import pl.polsl.photoplus.model.enums.OrderStatus;
import pl.polsl.photoplus.model.enums.PaymentMethod;
import pl.polsl.photoplus.repositories.OrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
public class OrderService extends AbstractModelService<Order, OrderModelDto, OrderRepository> {

    private final UserService userService;
    private final OrderItemService orderItemService;

    public OrderService(final OrderRepository entityRepository, final UserService userService, final OrderItemService orderItemService) {
        super(entityRepository);
        this.userService = userService;
        this.orderItemService = orderItemService;
    }

    @Override
    protected String getModelNameForError() {
        return "order";
    }

    @Override
    protected OrderModelDto getDtoFromModel(final Order modelObject) {
        final List<String> orderItemsCodes = new ArrayList<>();
        modelObject.getOrderItems().forEach(el -> {
            orderItemsCodes.add(el.getCode());
        });
        return new OrderModelDto(modelObject.getCode(), modelObject.getUser().getCode(), modelObject.getOrderStatus().name(),
                modelObject.getPaymentMethod().name(), modelObject.getPrice(), orderItemsCodes);
    }

    @Override
    protected Order getModelFromDto(final OrderModelDto dtoObject) {
        return new Order(OrderStatus.getOrderStatusFromString(dtoObject.getOrderStatus()),
                PaymentMethod.getPaymentMethodFromString(dtoObject.getPaymentMethod()), dtoObject.getPrice());
    }

    @Override
    public HttpStatus save(final List<OrderModelDto> dto) {

        final Function<OrderModelDto, Order> insertDependencies = orderModelDto -> {
            final Order orderToAdd = getModelFromDto(orderModelDto);

            final User userToInsert = userService.findByCodeOrThrowError(orderModelDto.getUserCode(),
                    "SAVE USER(customer)");
            orderToAdd.setUser(userToInsert);

            final List<OrderItem> orderItemList = new ArrayList<>();
            orderModelDto.getOrderItemsCode().forEach(orderItemCode -> {
                orderItemList.add(orderItemService.findByCodeOrThrowError(orderItemCode,
                            "SAVE ORDERITEM"));
            });
            orderToAdd.setOrderItems(orderItemList);

            return orderToAdd;
        };

        dto.stream().map(insertDependencies).forEach(entityRepository::save);
        return HttpStatus.CREATED;
    }
}
