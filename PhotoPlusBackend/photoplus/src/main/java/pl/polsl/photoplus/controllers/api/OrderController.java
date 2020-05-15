package pl.polsl.photoplus.controllers.api;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.polsl.photoplus.model.dto.OrderModelDto;
import pl.polsl.photoplus.model.dto.OrderModelDtoWithOrderItems;
import pl.polsl.photoplus.model.enums.OrderStatus;
import pl.polsl.photoplus.security.services.PermissionEvaluatorService;
import pl.polsl.photoplus.services.controllers.OrderService;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/order")
public class OrderController extends BaseModelController<OrderModelDto,OrderService> {

    private final String CUSTOMER_RELATION_NAME = "customer";

    public OrderController(final OrderService dtoService, final PermissionEvaluatorService permissionEvaluatorService) {
        super(dtoService, "order", permissionEvaluatorService);
    }

    @Override
    public void addLinks(final OrderModelDto dto) {
        dto.add(linkTo(methodOn(OrderController.class).getSingle(dto.getCode())).withSelfRel());
        dto.add(linkTo(methodOn(OrderController.class).delete(dto.getCode())).withRel(DELETE_RELATION_NAME));
        dto.add(linkTo(methodOn(UserController.class).getSingle(dto.getUserCode())).withRel(CUSTOMER_RELATION_NAME));
    }

    @PostMapping("/buy")
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, this.authorizationPrefix, 'post' )")
    public ResponseEntity saveOrderWithOrderItems(@RequestBody @Valid final OrderModelDtoWithOrderItems dto) {
        return new ResponseEntity(dtoService.saveWithItems(dto));
    }


    @GetMapping(path = "/{page}", produces = "application/json", params = "orderStatus")
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, this.authorizationPrefix, 'all' )")
    public ResponseEntity<List<OrderModelDto>> getAll(@PathVariable("page") final Integer page,
                                                      @RequestParam final OrderStatus orderStatus)
    {
        final List<OrderModelDto> dtos = dtoService.getPage(page, orderStatus);
        addLinks(dtos);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping(path = "/page/count", produces = "application/json", params = "orderStatus")
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, this.authorizationPrefix, 'all' )")
    public ResponseEntity<ObjectNode> getAmountOfPages(@RequestParam final OrderStatus orderStatus)
    {
        return new ResponseEntity<>(dtoService.getPageCount(orderStatus), HttpStatus.OK);
    }
}
