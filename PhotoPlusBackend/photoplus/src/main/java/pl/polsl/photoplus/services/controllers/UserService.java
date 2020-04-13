package pl.polsl.photoplus.services.controllers;

import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.dto.UserModelDto;
import pl.polsl.photoplus.model.entities.User;
import pl.polsl.photoplus.model.enums.UserRole;
import pl.polsl.photoplus.repositories.UserRepository;

@Service
public class UserService
        extends AbstractModelService<User,UserModelDto,UserRepository>
{
    final UserRepository userRepository;

    public UserService(final UserRepository userRepository)
    {
        super(userRepository);
        this.userRepository = userRepository;
    }

    @Override
    protected String getModelNameForError()
    {
        return "User";
    }

    @Override
    protected UserModelDto getDtoFromModel(final User modelObject)
    {
        return new UserModelDto(modelObject.getLogin(), modelObject.getEmail(), modelObject.getName(), modelObject.getSurname(), modelObject
                .getPassword(), modelObject.getNumber(), modelObject.getCode(), modelObject.getUserRole().getValue());
    }

    @Override
    protected User getModelFromDto(final UserModelDto dtoObject)
    {
        return new User(dtoObject.getLogin(), dtoObject.getEmail(), dtoObject.getName(), dtoObject.getSurname(), dtoObject
                .getPassword(), dtoObject.getNumber(), UserRole.getUserRoleFromString(dtoObject.getUserRole()));
    }

}
