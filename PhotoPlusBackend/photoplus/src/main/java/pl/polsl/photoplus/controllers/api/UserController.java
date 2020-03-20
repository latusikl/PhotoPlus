package pl.polsl.photoplus.controllers.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.photoplus.model.dto.UserModelDto;
import pl.polsl.photoplus.services.controllers.ModelRequestService;

@RestController
@RequestMapping("/user")
public class UserController extends BaseModelController<UserModelDto>
{

    final ModelRequestService<UserModelDto> modelRequestService;

    public UserController(final ModelRequestService modelRequestService)
    {
        super(modelRequestService);
        this.modelRequestService = modelRequestService;
    }
}
