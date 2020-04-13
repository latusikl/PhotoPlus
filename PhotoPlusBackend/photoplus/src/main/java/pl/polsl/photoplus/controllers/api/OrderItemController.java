package pl.polsl.photoplus.controllers.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.photoplus.model.dto.OrderItemModelDto;
import pl.polsl.photoplus.services.controllers.OrderItemService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequestMapping("/orderItem")
@RestController
public class OrderItemController extends BaseModelController<OrderItemModelDto> {

    private final String ORDER_RELATION_NAME = "order";
    private final String PRODUCT_RELATION_NAME = "product";

    public OrderItemController(final OrderItemService dtoService) {
        super(dtoService, "/orderItem");
    }

    @Override
    public void addLinks(final OrderItemModelDto dto) {
        dto.add(linkTo(methodOn(OrderItemController.class).getSingle(dto.getCode())).withSelfRel());
        dto.add(linkTo(methodOn(OrderItemController.class).delete(dto.getCode())).withRel(DELETE_RELATION_NAME));
        dto.add(linkTo(methodOn(OrderController.class).getSingle(dto.getOrderCode())).withRel(ORDER_RELATION_NAME));
        dto.add(linkTo(methodOn(ProductController.class).getSingle(dto.getProductCode())).withRel(PRODUCT_RELATION_NAME));
    }
}
