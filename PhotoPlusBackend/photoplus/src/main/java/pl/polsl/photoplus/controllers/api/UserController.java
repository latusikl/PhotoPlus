package pl.polsl.photoplus.controllers.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.photoplus.model.dto.UserModelDto;
import pl.polsl.photoplus.services.controllers.AddressService;
import pl.polsl.photoplus.services.controllers.UserService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/user")
public class UserController extends BaseModelController<UserModelDto,UserService>
{
    private final AddressService addressService;
    private final static String ADDRESS_RELATION_NAME = "address";

    public UserController(final UserService userService, final AddressService addressService)
    {
        super(userService, "user");
        this.addressService = addressService;
    }

    @Override
    public void addLinks(final UserModelDto dto)
    {
        dto.add(linkTo(methodOn(UserController.class).getSingle(dto.getCode())).withSelfRel());
        dto.add(linkTo(methodOn(UserController.class).delete(dto.getCode())).withRel(DELETE_RELATION_NAME));
        addressService.getUserAddresses(dto.getCode()).forEach(address ->
                dto.add(linkTo(methodOn(AddressController.class).getSingle(address.getCode())).withRel(ADDRESS_RELATION_NAME))
        );
    }
}
