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
    private final UserService userService;

    public AddressService(final AddressRepository addressRepository, final UserService userService)
    {
        super(addressRepository);
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

    private Address insertUserDependencyAndParseToModel(final AddressModelDto dto) {
        final User userToInsert = userService.findByCodeOrThrowError(dto.getUserCode(), "SAVE ADDRESS");
        final Address addressToAdd = getModelFromDto(dto);
        addressToAdd.setAddressOwner(userToInsert);
        return addressToAdd;
    }

    @Override
    public String save(final AddressModelDto dto)
    {
        final String addressCode = entityRepository.save(insertUserDependencyAndParseToModel(dto)).getCode();
        return addressCode;
    }

    @Override
    public HttpStatus saveAll(final List<AddressModelDto> dto)
    {
        dto.stream().map(this::insertUserDependencyAndParseToModel).forEach(entityRepository::save);
        return HttpStatus.OK;
    }

    public List<AddressModelDto> getUserAddresses(final String userCode)
    {
        return getDtoListFromModels(this.entityRepository.getAllByAddressOwner_Code(userCode));
    }

    public String getOwnerCode(final String code){
        return findByCodeOrThrowError(code,"getOwnerCode").getOwnerCode();
    }
}
