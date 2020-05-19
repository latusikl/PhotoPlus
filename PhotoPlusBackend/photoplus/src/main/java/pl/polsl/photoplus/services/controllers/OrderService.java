package pl.polsl.photoplus.services.controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.dto.OrderItemModelDto;
import pl.polsl.photoplus.model.dto.OrderModelDto;
import pl.polsl.photoplus.model.dto.OrderModelDtoWithOrderItems;
import pl.polsl.photoplus.model.dto.ProductModelDto;
import pl.polsl.photoplus.model.entities.Address;
import pl.polsl.photoplus.model.entities.Order;
import pl.polsl.photoplus.model.entities.User;
import pl.polsl.photoplus.model.enums.OrderStatus;
import pl.polsl.photoplus.model.enums.PaymentMethod;
import pl.polsl.photoplus.repositories.OrderRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class OrderService
        extends AbstractModelService<Order,OrderModelDto,OrderRepository>
{

    private final UserService userService;

    private final OrderItemService orderItemService;

    private final ProductService productService;

    private final BatchService batchService;

    private final AddressService addressService;

    public OrderService(final OrderRepository entityRepository, final UserService userService, final OrderItemService orderItemService, final ProductService productService, final BatchService batchService, final AddressService addressService)
    {
        super(entityRepository);
        this.userService = userService;
        this.orderItemService = orderItemService;
        this.productService = productService;
        this.batchService = batchService;
        this.addressService = addressService;
    }

    @Override
    protected String getModelNameForError()
    {
        return "order";
    }

    @Override
    protected OrderModelDto getDtoFromModel(final Order modelObject)
    {
        return new OrderModelDto(modelObject.getCode(), modelObject.getUser().getCode(), modelObject.getAddress()
                .getCode(), modelObject.getOrderStatus().name(), modelObject.getPaymentMethod()
                                         .name(), modelObject.getPrice(), modelObject.getDate());
    }

    @Override
    protected Order getModelFromDto(final OrderModelDto dtoObject)
    {
        return new Order(OrderStatus.getOrderStatusFromString(dtoObject.getOrderStatus()), PaymentMethod.getPaymentMethodFromString(dtoObject
                                                                                                                                            .getPaymentMethod()), dtoObject
                                 .getPrice(), dtoObject.getDate());
    }

    @Override
    public HttpStatus saveAll(final List<OrderModelDto> dto)
    {
        dto.stream().map(this::insertDependenciesAndParseToModel).forEach(entityRepository::save);
        return HttpStatus.CREATED;
    }

    @Override
    public String save(final OrderModelDto dto)
    {
        final String entityCode = entityRepository.save(insertDependenciesAndParseToModel(dto)).getCode();
        return entityCode;
    }

    private Order insertDependenciesAndParseToModel(final OrderModelDto orderModelDto)
    {
        final User userToInsert = userService.findByCodeOrThrowError(orderModelDto.getUserCode(), "SAVE ORDER");
        final Address addressToInsert = addressService.findByCodeOrThrowError(orderModelDto.getAddressCode(), "SAVE ORDER");
        final Order orderToAdd = getModelFromDto(orderModelDto);
        orderToAdd.setUser(userToInsert);
        orderToAdd.setAddress(addressToInsert);
        return orderToAdd;
    }

    public ResponseEntity<List<String>> getOrderBasicFromUser(final String userCode)
    {
        return new ResponseEntity<>(entityRepository.findAllBaseInfoByUser(userCode), HttpStatus.CREATED);
    }

    @Transactional
    public HttpStatus saveWithItems(final OrderModelDtoWithOrderItems dto)
    {
        //Create Order
        final Order orderModel = insertDependenciesAndParseToModel(dto);
        entityRepository.save(orderModel);
        final List<OrderItemModelDto> orderItems = dto.getOrderItemModelDtos();
        Double orderPrice = 0.0;

        for (final OrderItemModelDto orderItem : orderItems) {
            productService.subStoreQuantity(orderItem.getProductCode(), orderItem.getQuantity());
            batchService.subStoreQuantity(orderItem.getProductCode(), orderItem.getQuantity());

            final ProductModelDto productDto = productService.getSingleObject(orderItem.getProductCode());
            orderPrice += productDto.getPrice() * orderItem.getQuantity();
            orderItem.setOrderCode(orderModel.getCode());
        }

        orderModel.setPrice(orderPrice);
        //set price of order
        entityRepository.save(orderModel);
        orderItemService.saveAll(orderItems);
        return HttpStatus.CREATED;
    }

    public List<OrderModelDto> getPage(final Integer pageNumber, final OrderStatus orderStatus)
    {
        return getDtoListFromModels(this.getModelPage(pageNumber, orderStatus));
    }

    private Page<Order> getModelPage(final Integer pageNumber, final OrderStatus orderStatus)
    {
        final Pageable modelPage = PageRequest.of(pageNumber, modelPropertiesService.getPageSize());
        final Page<Order> foundModels = entityRepository.findAllByOrderStatus(modelPage, orderStatus);
        return foundModels;
    }

    public ObjectNode getPageCount(final OrderStatus orderStatus)
    {

        final Page<Order> firstPage = getModelPage(0, orderStatus);
        final ObjectNode jsonNode = objectMapper.createObjectNode();

        jsonNode.put("pageAmount", firstPage.getTotalPages());
        jsonNode.put("pageSize", modelPropertiesService.getPageSize());

        return jsonNode;
    }

    public String getOwnerCode(final String code){
        return findByCodeOrThrowError(code,"getOwnerCode").getUser().getCode();
    }
}
