package pl.polsl.photoplus.controllers.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.photoplus.model.dto.UserModelDto;
import pl.polsl.photoplus.services.ModelPatchService;
import pl.polsl.photoplus.services.controllers.UserService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/user")
public class UserController
        extends BaseModelController<UserModelDto>
{

    final UserService userService;

    final ModelPatchService modelPatchService;

    protected UserController(final UserService userService, final ModelPatchService modelPatchService)
    {
        super(userService);
        this.userService = userService;
        this.modelPatchService = modelPatchService;
    }

    @Override
    public void addLinks(final UserModelDto dto)
    {
        //TODO: Add link to user's addresses!!
        dto.add(linkTo(methodOn(UserController.class).getSingle(dto.getCode())).withSelfRel());
        dto.add(linkTo(methodOn(UserController.class).delete(dto.getCode())).withRel(DELETE_RELATION_NAME));
    }
}
