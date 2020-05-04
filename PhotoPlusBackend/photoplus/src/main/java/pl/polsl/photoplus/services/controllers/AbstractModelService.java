package pl.polsl.photoplus.services.controllers;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import pl.polsl.photoplus.components.ModelPropertiesService;
import pl.polsl.photoplus.model.dto.AbstractModelDto;
import pl.polsl.photoplus.model.entities.AbstractEntityModel;
import pl.polsl.photoplus.repositories.EntityRepository;
import pl.polsl.photoplus.services.ModelPatchService;
import pl.polsl.photoplus.services.controllers.exceptions.NotFoundException;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Abstract service containing handlers for base API requests.
 *
 * @param <M> Model entity class extending AbstractModel.
 * @param <T> DTO class extending AbstractDto.
 * @param <R> Repository for given model entity.
 *
 * @see pl.polsl.photoplus.controllers.api.BaseModelController
 * To use you need to provide implementation of 2 methods which provides convertion between model objects and DTOs.
 * Implements methods defined in
 * @see ModelService
 * @see AbstractEntityModel
 * @see AbstractModelDto
 * @see CrudRepository
 */
@Slf4j
public abstract class AbstractModelService<M extends AbstractEntityModel, T extends AbstractModelDto, R extends EntityRepository>
        implements ModelService<T>
{

    /**
     * Repository needs to be injected manually by calling super class constructor in extending class.
     */
    R entityRepository;

    @Autowired
    ModelPatchService modelPatchService;

    @Autowired
    ModelPropertiesService modelPropertiesService;

    public AbstractModelService(final R entityRepository)
    {
        this.entityRepository = entityRepository;
    }

    protected abstract String getModelNameForError();

    protected abstract T getDtoFromModel(final M modelObject);

    protected List<T> getDtoListFromModels(final Iterable<M> modelIterable)
    {
        final List<M> foundModelList = new ArrayList<>();
        modelIterable.forEach(foundModelList::add);
        return foundModelList.stream().map(this::getDtoFromModel).collect(Collectors.toList());
    }

    protected abstract M getModelFromDto(final T dtoObject);

    protected List<M> getModelListFromDtos(final Collection<T> dtoCollection)
    {
        return dtoCollection.stream().map(this::getModelFromDto).collect(Collectors.toList());
    }

    @Override
    public List<T> getPageFromAll(final Integer page)
    {
        final Pageable modelPage = PageRequest.of(page, modelPropertiesService.getPageSize());
        final Page<M> foundModels = entityRepository.findAll(modelPage);

        throwNotFoundErrorIfIterableEmpty("FIND ALL", foundModels);

        return getDtoListFromModels(foundModels);
    }

    @Override
    public T getSingleObject(final String code)
    {
        return getDtoFromModel(findByCodeOrThrowError(code, "FIND SINGLE"));
    }

    @Override
    public HttpStatus saveAll(final List<T> dto)
    {
        entityRepository.saveAll(getModelListFromDtos(dto));
        return HttpStatus.CREATED;
    }

    @Override
    public String save(final T dto)
    {
        final String entityCode = getModelFromDto(dto).getCode();
        entityRepository.save(getModelFromDto(dto));
        return entityCode;
    }

    @Override
    public HttpStatus delete(final String code)
    {
        entityRepository.delete(findByCodeOrThrowError(code, "DELETE"));
        return HttpStatus.NO_CONTENT;
    }

    @Override
    public HttpStatus patch(final T dtoPatch, final String code)
    {
        final M foundModelObject = findByCodeOrThrowError(code, "PATCH");
        modelPatchService.applyPatch(foundModelObject, dtoPatch);
        entityRepository.save(foundModelObject);
        return HttpStatus.OK;
    }

    @Override
    public List<T> getAll()
    {
        final Iterable<M> foundModels = entityRepository.findAll();
        throwNotFoundErrorIfIterableEmpty("FIND ALL", foundModels);
        return getDtoListFromModels(foundModels);
    }

    private void throwNotFoundErrorIfIterableEmpty(final String methodName, final Iterable<?> iterable)
    {
        if (IterableUtils.size(iterable) == 0) {
            throwNotFoundError(methodName);
        }
    }

    public M findByCodeOrThrowError(final String code, final String sourceMethod)
    {
        final Optional<M> foundModelObject = entityRepository.findByCode(code);
        if (foundModelObject.isEmpty()) {
            throwNotFoundError(sourceMethod);
        }
        return foundModelObject.get();
    }

    protected void throwNotFoundError(final String sourceMethod)
    {
        log.info("{} | No objects of class {} found. Throwing NotFoundException.", sourceMethod, getModelNameForError());
        throw new NotFoundException("Cannot find object of given code.", getModelNameForError());
    }

}
