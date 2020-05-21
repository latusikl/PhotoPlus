package pl.polsl.photoplus.services.controllers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.dto.UserModelDto;
import pl.polsl.photoplus.model.entities.User;
import pl.polsl.photoplus.model.enums.UserRole;
import pl.polsl.photoplus.repositories.UserRepository;
import pl.polsl.photoplus.services.controllers.exceptions.CannotDeleteUserException;

import java.util.List;

@Service
public class UserService
        extends AbstractModelService<User, UserModelDto, UserRepository> implements FieldValueExists
{

    public UserService(final UserRepository userRepository)
    {
        super(userRepository);
    }

    @Override
    public List<UserModelDto> getAll()
    {
        final List<User> foundModels = entityRepository.findAllByOrderByLogin();
        return getDtoListFromModels(foundModels);
    }

    @Override
    public List<UserModelDto> getPageFromAll(final Integer page)
    {
        return getDtoListFromModels(getPage(page));
    }


    private Page<User> getPage(final Integer pageNumber)
    {
        final Pageable modelPage = PageRequest.of(pageNumber, modelPropertiesService.getPageSize());
        final Page<User> foundModels = entityRepository.findAllByOrderByLogin(modelPage);

        throwNotFoundErrorIfIterableEmpty("FIND ALL", foundModels);
        return foundModels;
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

    @Override
    public boolean fieldValueExists(final String value, final String fieldName)
    {
        if (fieldName.equals("email")) {
            return this.entityRepository.findUserByEmail(value).isPresent();
        } else if (fieldName.equals("login")) {
            return this.entityRepository.findUserByLogin(value).isPresent();
        }
        return false;
    }

    public List<UserModelDto> getByLoginContainingStr(final String str)
    {
        return getDtoListFromModels(entityRepository.findByLoginContainingIgnoreCase(str));
    }

    public HttpStatus deleteUser(final String code) throws CannotDeleteUserException
    {
        final User user = findByCodeOrThrowError(code, "DELETE");
        try {
            entityRepository.delete(user);
        } catch (final DataIntegrityViolationException e) {
            user.setName("ANONYMOUS_DELETED");
            user.setSurname("ANONYMOUS_DELETED");
            user.setEnabled(false);
            entityRepository.save(user);
            throw new CannotDeleteUserException("Cannot delete user with login " + user.getLogin() + " and code "
                    + user.getCode() + " as there is some corresponding data.\n" +
                    "Deleting would result in violation of an integrity constraint. Account is disabled.", User.class.getSimpleName());
        }
        return HttpStatus.NO_CONTENT;
    }
}
