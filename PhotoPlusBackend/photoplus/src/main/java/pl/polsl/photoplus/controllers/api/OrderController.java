package pl.polsl.photoplus.controllers.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.photoplus.model.dto.OrderModelDto;
import pl.polsl.photoplus.model.dto.OrderModelDtoWithOrderItems;
import pl.polsl.photoplus.services.controllers.OrderService;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/order")
public class OrderController extends BaseModelController<OrderModelDto,OrderService> {

    private final String CUSTOMER_RELATION_NAME = "customer";

    public OrderController(final OrderService dtoService) {
        super(dtoService, "order");
    }

    @Override
    public void addLinks(final OrderModelDto dto) {
        dto.add(linkTo(methodOn(OrderController.class).getSingle(dto.getCode())).withSelfRel());
        dto.add(linkTo(methodOn(OrderController.class).delete(dto.getCode())).withRel(DELETE_RELATION_NAME));
        dto.add(linkTo(methodOn(UserController.class).getSingle(dto.getUserCode())).withRel(CUSTOMER_RELATION_NAME));
    }

    @PostMapping("/buy")
    @PreAuthorize("hasPermission(this.authorizationPrefix, 'post' )")
    public ResponseEntity saveOrderWithOrderItems(@RequestBody @Valid final List<OrderModelDtoWithOrderItems> orderModelDtoWithItems) {
        return new ResponseEntity(dtoService.saveWithItems(orderModelDtoWithItems));
    }
}
