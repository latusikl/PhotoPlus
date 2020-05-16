package pl.polsl.photoplus.controllers.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.polsl.photoplus.model.dto.AddressModelDto;
import pl.polsl.photoplus.security.services.PermissionEvaluatorService;
import pl.polsl.photoplus.services.controllers.AddressService;

import java.util.List;

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

    @GetMapping(path = "/byUser/{code}", produces = {"application/json"})
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, #code)")
    public ResponseEntity<List<AddressModelDto>> getAllByUserCode(@PathVariable("code") final String code)
    {
        final List<AddressModelDto> dtos = this.dtoService.getUserAddresses(code);
        addLinks(dtos);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PatchMapping("/editAddress/{code}")
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, #code)")
    public ResponseEntity patch(@RequestBody final AddressModelDto dtoPatch, @PathVariable("code") final String code)
    {
        return new ResponseEntity(dtoService.patch(dtoPatch, code));
    }
}
