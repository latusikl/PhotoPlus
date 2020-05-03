package pl.polsl.photoplus.services.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.dto.OrderItemModelDto;
import pl.polsl.photoplus.model.dto.OrderModelDto;
import pl.polsl.photoplus.model.dto.OrderModelDtoWithOrderItems;
import pl.polsl.photoplus.model.entities.Order;
import pl.polsl.photoplus.model.entities.User;
import pl.polsl.photoplus.model.enums.OrderStatus;
import pl.polsl.photoplus.model.enums.PaymentMethod;
import pl.polsl.photoplus.repositories.OrderRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class OrderService extends AbstractModelService<Order, OrderModelDto, OrderRepository> {

    private final UserService userService;
    private final OrderItemService orderItemService;
    private final ProductService productService;
    private final BatchService batchService;


    public OrderService(final OrderRepository entityRepository, final UserService userService,
                        final OrderItemService orderItemService, final ProductService productService,
                        final BatchService batchService) {
        super(entityRepository);
        this.userService = userService;
        this.orderItemService = orderItemService;
        this.productService = productService;
        this.batchService = batchService;
    }

    @Override
    protected String getModelNameForError() {
        return "order";
    }

    @Override
    protected OrderModelDto getDtoFromModel(final Order modelObject) {
        return new OrderModelDto(modelObject.getCode(), modelObject.getUser().getCode(), modelObject.getOrderStatus().name(),
                modelObject.getPaymentMethod().name(), modelObject.getPrice(), modelObject.getDate());
    }

    @Override
    protected Order getModelFromDto(final OrderModelDto dtoObject) {
        return new Order(OrderStatus.getOrderStatusFromString(dtoObject.getOrderStatus()),
                PaymentMethod.getPaymentMethodFromString(dtoObject.getPaymentMethod()), dtoObject.getPrice(),
                dtoObject.getDate());
    }

    private Order insertUserDependencyAndParseToModel(final OrderModelDto orderModelDto) {
        final User userToInsert = userService.findByCodeOrThrowError(orderModelDto.getUserCode(),
                "SAVE USER(customer)");
        final Order orderToAdd = getModelFromDto(orderModelDto);
        orderToAdd.setUser(userToInsert);
        return orderToAdd;
    }

    @Override
    public HttpStatus save(final List<OrderModelDto> dto) {
        dto.stream().map(this::insertUserDependencyAndParseToModel).forEach(entityRepository::save);
        return HttpStatus.CREATED;
    }

    @Transactional
    public HttpStatus saveWithItems(final List<OrderModelDtoWithOrderItems> orderModelDtoWithItems) {
        //Create Order
        orderModelDtoWithItems.forEach(orderDto -> {
            final Order orderModel = insertUserDependencyAndParseToModel(orderDto);
            entityRepository.save(orderModel);
            final List<OrderItemModelDto> orderItems = orderDto.getOrderItemModelDtos();

            orderItems.forEach(orderItem -> {
                productService.subStoreQuantity(orderItem.getProductCode(), orderItem.getQuantity());
                batchService.subStoreQuantity(orderItem.getProductCode(), orderItem.getQuantity());
                orderItem.setOrderCode(orderModel.getCode());
            });
            orderItemService.save(orderItems);
        });
        return HttpStatus.CREATED;
    }
}
