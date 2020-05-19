package pl.polsl.photoplus.controllers.api;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.polsl.photoplus.model.dto.OrderItemModelDto;
import pl.polsl.photoplus.security.services.PermissionEvaluatorService;
import pl.polsl.photoplus.services.controllers.AddressService;
import pl.polsl.photoplus.services.controllers.OrderItemService;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequestMapping("/orderItem")
@RestController
public class OrderItemController extends BaseModelController<OrderItemModelDto,OrderItemService> {

    private final String ORDER_RELATION_NAME = "order";
    private final String PRODUCT_RELATION_NAME = "product";

    public OrderItemController(final OrderItemService dtoService, final PermissionEvaluatorService permissionEvaluatorService) {
        super(dtoService, "orderItem", permissionEvaluatorService);
    }

    @Override
    public void addLinks(final OrderItemModelDto dto) {
        dto.add(linkTo(methodOn(OrderItemController.class).getSingle(dto.getCode())).withSelfRel());
        dto.add(linkTo(methodOn(OrderItemController.class).delete(dto.getCode())).withRel(DELETE_RELATION_NAME));
        dto.add(linkTo(methodOn(OrderController.class).getSingle(dto.getOrderCode())).withRel(ORDER_RELATION_NAME));
        dto.add(linkTo(methodOn(ProductController.class).getSingle(dto.getProductCode())).withRel(PRODUCT_RELATION_NAME));
    }

    @GetMapping(path = "/{page}", produces = "application/json", params = "orderCode")
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, this.authorizationPrefix, 'all' ) or @permissionEvaluatorService.hasPrivilege(authentication, this.getService().getOwnerCode(#orderCode))" )
    public ResponseEntity<List<OrderItemModelDto>> getAll(@PathVariable("page") final Integer page,
                                                      @RequestParam final String orderCode)
    {
        final List<OrderItemModelDto> dtos = dtoService.getPage(page, orderCode);
        addLinks(dtos);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping(path = "/page/count", produces = "application/json", params = "orderCode")
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, this.authorizationPrefix, 'all' ) or @permissionEvaluatorService.hasPrivilege(authentication, this.getService().getOwnerCode(#orderCode))")
    public ResponseEntity<ObjectNode> getAmountOfPages(@RequestParam final String orderCode)
    {
        return new ResponseEntity<>(dtoService.getPageCount(orderCode), HttpStatus.OK);
    }

    /**
     * You need to use getter to use service in @PreAuthorize because SpEL cannot use inherited field.
     */
    public OrderItemService getService()
    {
        return this.dtoService;
    }
}
