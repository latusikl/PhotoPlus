package pl.polsl.photoplus.services.controllers;

import org.springframework.http.HttpStatus;
import pl.polsl.photoplus.model.dto.AbstractModelDto;

import java.util.List;
import java.util.Set;

/**
 * Common interface for all services responsible for interacting with DTO objects.
 * It defines basic API operations which should be provided for DTOs and their models.
 *
 * @param <T> Type of DTO
 */
public interface ModelRequestService<T extends AbstractModelDto>
{
    List<T> getPageFromAll(Integer page);

    T getSingleObject(String code);

    HttpStatus save(Set<T> dto);

    HttpStatus delete(String code);

    HttpStatus patch(T dtoPatch, String code);
}
