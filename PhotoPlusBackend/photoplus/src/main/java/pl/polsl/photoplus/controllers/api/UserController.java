package pl.polsl.photoplus.controllers.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.polsl.photoplus.model.dto.UserModelDto;
import pl.polsl.photoplus.model.enums.UserRole;
import pl.polsl.photoplus.security.services.PermissionEvaluatorService;
import pl.polsl.photoplus.services.controllers.AddressService;
import pl.polsl.photoplus.services.controllers.UserService;
import pl.polsl.photoplus.services.controllers.exceptions.CannotDeleteUserException;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/user")
public class UserController
        extends BaseModelController<UserModelDto,UserService>
{
    private final static String ADDRESS_RELATION_NAME = "address";

    private final AddressService addressService;

    public UserController(final UserService userService, final AddressService addressService, final PermissionEvaluatorService permissionEvaluatorService)
    {
        super(userService, "user", permissionEvaluatorService);
        this.addressService = addressService;
    }

    @Override
    public void addLinks(final UserModelDto dto)
    {
        dto.add(linkTo(methodOn(UserController.class).getSingle(dto.getCode())).withSelfRel());
        dto.add(linkTo(methodOn(UserController.class).delete(dto.getCode())).withRel(DELETE_RELATION_NAME));
        addressService.getUserAddresses(dto.getCode())
                .forEach(address -> dto.add(linkTo(methodOn(AddressController.class).getSingle(address.getCode())).withRel(ADDRESS_RELATION_NAME)));
    }

    @PostMapping("/register")
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, this.authorizationPrefix, 'register' )")
    public ResponseEntity register(@RequestBody @Valid final UserModelDto dto)
    {
        dto.setUserRole(UserRole.CLIENT.getValue());
        return post(dto);
    }

    @PatchMapping("/editAccount/{code}")
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, #code)")
    public ResponseEntity patchOwnAccount(@RequestBody final UserModelDto dtoPatch, @PathVariable(
            "code") final String code)
    {
        return new ResponseEntity(dtoService.patch(dtoPatch, code));
    }

    @GetMapping("/editAccount/{code}")
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, #code)")
    public ResponseEntity<UserModelDto> getOwnAccount(@PathVariable("code") final String code)
    {
        return new ResponseEntity<>(dtoService.getSingleObject(code), HttpStatus.OK);
    }

    @GetMapping("/search/{str}")
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, this.authorizationPrefix, 'all' )")
    public ResponseEntity searchByLogin(@PathVariable("str") final String str)
    {
        return new ResponseEntity(dtoService.getByLoginContainingStr(str), HttpStatus.OK);
    }

    @DeleteMapping("/deleteUser/{code}")
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, this.authorizationPrefix, 'delete' )")
    public ResponseEntity deleteUser(@PathVariable final String code) throws CannotDeleteUserException {
        return new ResponseEntity(dtoService.deleteUser(code));
    }
}
