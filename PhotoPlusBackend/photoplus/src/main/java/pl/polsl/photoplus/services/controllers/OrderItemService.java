package pl.polsl.photoplus.services.controllers;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.dto.OrderItemModelDto;
import pl.polsl.photoplus.model.entities.Order;
import pl.polsl.photoplus.model.entities.OrderItem;
import pl.polsl.photoplus.model.entities.Product;
import pl.polsl.photoplus.repositories.OrderItemRepository;

import javax.transaction.Transactional;
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
    private OrderItem insertDependenciesAndParseToModel(final OrderItemModelDto dto) {
        final Product productToInsert = productService.findByCodeOrThrowError(dto.getProductCode(),
                "SAVE PRODUCT");
        final Order orderToInsert = orderService.findByCodeOrThrowError(dto.getOrderCode(),
                "SAVE ORDER");
        final OrderItem orderItemToAdd = getModelFromDto(dto);
        orderItemToAdd.setProduct(productToInsert);
        orderItemToAdd.setOrder(orderToInsert);
        return orderItemToAdd;
    };

    @Override
    @Transactional
    public String save(final OrderItemModelDto dto) {
        final String entityCode = entityRepository.save(insertDependenciesAndParseToModel(dto)).getCode();
        return entityCode;
    }

    @Override
    @Transactional
    public HttpStatus saveAll(final List<OrderItemModelDto> dto) {
        dto.stream().map(this::insertDependenciesAndParseToModel).forEach(entityRepository::save);
        return HttpStatus.CREATED;
    }

    public List<OrderItemModelDto> getAllByOrderCode(final String orderCode) {
        return getDtoListFromModels(entityRepository.getAllByOrder_Code(orderCode));
    }
}
