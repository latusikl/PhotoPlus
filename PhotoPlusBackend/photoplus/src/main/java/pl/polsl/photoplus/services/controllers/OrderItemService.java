package pl.polsl.photoplus.services.controllers;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.dto.OrderItemModelDto;
import pl.polsl.photoplus.model.entities.Order;
import pl.polsl.photoplus.model.entities.OrderItem;
import pl.polsl.photoplus.model.entities.Product;
import pl.polsl.photoplus.repositories.OrderItemRepository;

import java.util.List;
import java.util.function.Function;

@Service
public class OrderItemService extends AbstractModelService<OrderItem, OrderItemModelDto, OrderItemRepository> {

    private OrderService orderService;

    private ProductService productService;

    public OrderItemService(final OrderItemRepository entityRepository, @Lazy final OrderService orderService,
                            final ProductService productService) {
        super(entityRepository);
        this.orderService = orderService;
        this.productService = productService;
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

    @Override
    public HttpStatus save(final List<OrderItemModelDto> dto) {
        final Function<OrderItemModelDto, OrderItem> insertDependenciesAndParseToModel = orderModelDto -> {
            final Product productToInsert = productService.findByCodeOrThrowError(orderModelDto.getProductCode(),
                    "SAVE PRODUCT");
            final Order orderToInsert = orderService.findByCodeOrThrowError(orderModelDto.getOrderCode(),
                    "SAVE ORDER");
            final OrderItem orderItemToAdd = getModelFromDto(orderModelDto);
            orderItemToAdd.setProduct(productToInsert);
            orderItemToAdd.setOrder(orderToInsert);
            return orderItemToAdd;
        };

        dto.stream().map(insertDependenciesAndParseToModel).forEach(entityRepository::save);
        return HttpStatus.CREATED;
    }
}
