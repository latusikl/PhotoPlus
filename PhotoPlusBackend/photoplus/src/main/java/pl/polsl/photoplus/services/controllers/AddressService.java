package pl.polsl.photoplus.services.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.dto.AddressModelDto;
import pl.polsl.photoplus.model.entities.Address;
import pl.polsl.photoplus.model.entities.User;
import pl.polsl.photoplus.repositories.AddressRepository;

import java.util.List;
import java.util.function.Function;

@Service
public class AddressService
        extends AbstractModelService<Address,AddressModelDto,AddressRepository>
{
    private final AddressRepository addressRepository;

    private final UserService userService;

    public AddressService(final AddressRepository addressRepository, final UserService userService)
    {
        super(addressRepository);
        this.addressRepository = addressRepository;
        this.userService = userService;
    }

    @Override
    protected String getModelNameForError()
    {
        return "Address";
    }

    @Override
    protected AddressModelDto getDtoFromModel(final Address modelObject)
    {
        return new AddressModelDto(modelObject.getCode(), modelObject.getStreet(), modelObject.getNumber(), modelObject.getZipCode(), modelObject
                .getCity(), modelObject.getCountryCode(), modelObject.getOwnerCode());
    }

    @Override
    protected Address getModelFromDto(final AddressModelDto dtoObject)
    {
        return new Address(dtoObject.getStreet(), dtoObject.getNumber(), dtoObject.getZipCode(), dtoObject.getCity(), dtoObject
                .getCountryCode());
    }

    @Override
    public HttpStatus save(final List<AddressModelDto> dto)
    {
        final Function<AddressModelDto,Address> insertUserDepedencyAndParseToModel = addressModelDto -> {
            final User userToInsert = userService.findByCodeOrThrowError(addressModelDto.getUserCode(), "SAVE ADDRESS");
            final Address addressToAdd = getModelFromDto(addressModelDto);
            addressToAdd.setAddressOwner(userToInsert);
            return addressToAdd;
        };

        dto.stream().map(insertUserDepedencyAndParseToModel).forEach(addressRepository::save);
        return HttpStatus.OK;
    }
}
