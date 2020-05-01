package pl.polsl.photoplus.controllers.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.photoplus.model.dto.AddressModelDto;
import pl.polsl.photoplus.security.services.PermissionEvaluatorService;
import pl.polsl.photoplus.services.controllers.AddressService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/address")
public class AddressController extends  BaseModelController<AddressModelDto,AddressService>
{
    private final String OWNER_RELATION_NAME = "owner";

    public AddressController(final AddressService addressService, final PermissionEvaluatorService permissionEvaluatorService)
    {
        super(addressService, "address", permissionEvaluatorService);
    }

    @Override
    public void addLinks(final AddressModelDto dto)
    {
        dto.add(linkTo(methodOn(AddressController.class).getSingle(dto.getCode())).withSelfRel());
        dto.add(linkTo(methodOn(AddressController.class).delete(dto.getCode())).withRel(DELETE_RELATION_NAME));
        dto.add(linkTo(methodOn(UserController.class).getSingle(dto.getUserCode())).withRel(OWNER_RELATION_NAME));
    }
}
