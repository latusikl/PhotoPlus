package pl.polsl.photoplus.controllers.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.photoplus.model.dto.AddressModelDto;
import pl.polsl.photoplus.security.services.PermissionEvaluatorService;
import pl.polsl.photoplus.services.controllers.AddressService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/address")
@Validated
public class AddressController
        extends BaseModelController<AddressModelDto,AddressService>
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

    @DeleteMapping("/editAddress/{code}")
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, this.getService().getOwnerCode(#code))")
    public ResponseEntity deleteForUser(@PathVariable("code") final String code)
    {
        return new ResponseEntity(dtoService.delete(code));
    }

    @PatchMapping("/editAddress/{code}")
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, this.getService().getOwnerCode(#code))")
    public ResponseEntity patchForUser(@RequestBody final AddressModelDto dtoPatch, @PathVariable("code") final String code)
    {
        return new ResponseEntity(dtoService.patch(dtoPatch, code));
    }

    @GetMapping(path = "/byUser/{code}", produces = {"application/json"})
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, #code)")
    public ResponseEntity<List<AddressModelDto>> getAllByUserCode(@PathVariable("code") final String code)
    {
        final List<AddressModelDto> dtos = this.dtoService.getUserAddresses(code);
        addLinks(dtos);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping(path = "/byUser/single/{code}", produces = {"application/json"})
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, this.getService().getOwnerCode(#code))")
    public ResponseEntity<AddressModelDto> getSingleByUserCode(@PathVariable("code") final String code)
    {
        return this.getSingle(code);
    }

    @PostMapping("/editAddress/{code}")
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, #code)")
    public String postForUser(@RequestBody @Valid final AddressModelDto dto, @PathVariable("code") final String code)
    {
        //Prevent form passing different code of user in dto than logged one
        dto.setUserCode(code);
        return dtoService.save(dto);
    }

    /**
     * You need to use getter to use service in @PreAuthorize because SpEL cannot use inherited field.
     */
    public AddressService getService()
    {
        return this.dtoService;
    }
}
