package pl.polsl.photoplus.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

/**
 * Base repository extending Spring CrudRepository.
 * But with adding additional methods which should be later provided for all Model repositories.
 * @param <T> Model type
 * @param <ID> Primary key
 */
@NoRepositoryBean
public interface EntityRepository<T,ID> extends CrudRepository<T,ID>
{
    Page<T> findAll(Pageable page);
    Optional<T> findByCode(String code);
}
