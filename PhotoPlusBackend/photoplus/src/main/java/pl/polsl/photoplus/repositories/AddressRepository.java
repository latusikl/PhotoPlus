package pl.polsl.photoplus.repositories;

import org.springframework.stereotype.Repository;
import pl.polsl.photoplus.model.entities.Address;

import java.util.List;

@Repository
public interface AddressRepository extends EntityRepository<Address,Long>
{
    List<Address> getAllByAddressOwner_Code(final String code);
}
