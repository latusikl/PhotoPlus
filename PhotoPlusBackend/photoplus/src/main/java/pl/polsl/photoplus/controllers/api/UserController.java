package pl.polsl.photoplus.controllers.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.photoplus.model.dto.UserModelDto;
import pl.polsl.photoplus.services.controllers.ModelRequestService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/user")
public class UserController
        extends BaseModelController<UserModelDto>
{

    final ModelRequestService<UserModelDto> modelRequestService;

    public UserController(final ModelRequestService modelRequestService)
    {
        super(modelRequestService);
        this.modelRequestService = modelRequestService;
    }

    @Override
    public void addLinks(final UserModelDto dto)
    {
        dto.add(linkTo(methodOn(UserController.class).getSingle(dto.getCode())).withSelfRel());
        dto.add(linkTo(methodOn(UserController.class).delete(dto.getCode())).withRel(DELETE_RELATION_NAME));
    }
}
