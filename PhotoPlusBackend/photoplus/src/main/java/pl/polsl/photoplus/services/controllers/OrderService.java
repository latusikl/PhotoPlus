package pl.polsl.photoplus.services.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.dto.OrderModelDto;
import pl.polsl.photoplus.model.entities.Order;
import pl.polsl.photoplus.model.entities.User;
import pl.polsl.photoplus.model.enums.OrderStatus;
import pl.polsl.photoplus.model.enums.PaymentMethod;
import pl.polsl.photoplus.repositories.OrderRepository;

import java.util.List;
import java.util.function.Function;

@Service
public class OrderService extends AbstractModelService<Order, OrderModelDto, OrderRepository> {

    private final UserService userService;

    public OrderService(final OrderRepository entityRepository, final UserService userService) {
        super(entityRepository);
        this.userService = userService;
    }

    @Override
    protected String getModelNameForError() {
        return "order";
    }

    @Override
    protected OrderModelDto getDtoFromModel(final Order modelObject) {
        return new OrderModelDto(modelObject.getCode(), modelObject.getUser().getCode(), modelObject.getOrderStatus().name(),
                modelObject.getPaymentMethod().name(), modelObject.getPrice());
    }

    @Override
    protected Order getModelFromDto(final OrderModelDto dtoObject) {
        return new Order(OrderStatus.getOrderStatusFromString(dtoObject.getOrderStatus()),
                PaymentMethod.getPaymentMethodFromString(dtoObject.getPaymentMethod()), dtoObject.getPrice());
    }

    @Override
    public HttpStatus save(final List<OrderModelDto> dto) {
        final Function<OrderModelDto, Order> insertUserDependencyAndParseToModel = orderModelDto -> {
            final User userToInsert = userService.findByCodeOrThrowError(orderModelDto.getUserCode(),
                    "SAVE USER(customer)");
            final Order orderToAdd = getModelFromDto(orderModelDto);
            orderToAdd.setUser(userToInsert);
            return orderToAdd;
        };

        dto.stream().map(insertUserDependencyAndParseToModel).forEach(entityRepository::save);
        return HttpStatus.CREATED;
    }
}
