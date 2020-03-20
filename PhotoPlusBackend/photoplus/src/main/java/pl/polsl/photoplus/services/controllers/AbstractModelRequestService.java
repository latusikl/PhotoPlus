package pl.polsl.photoplus.services.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import pl.polsl.photoplus.model.dto.AbstractModelDto;
import pl.polsl.photoplus.model.entities.AbstractEntityModel;
import pl.polsl.photoplus.repositories.EntityRepository;
import pl.polsl.photoplus.services.ModelPatchService;
import pl.polsl.photoplus.services.ModelPropertiesService;
import pl.polsl.photoplus.services.controllers.exceptions.NotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
 * @see ModelRequestService
 * @see AbstractEntityModel
 * @see AbstractModelDto
 * @see CrudRepository
 */
@Slf4j
public abstract class AbstractModelRequestService<M extends AbstractEntityModel, T extends AbstractModelDto, R extends EntityRepository>
        implements ModelRequestService<T>
{

    /**
     * Repository needs to be injected manually by calling super class constructor in extending class.
     */
    R entityRepository;

    @Autowired
    ModelPatchService modelPatchService;
    @Autowired
    ModelPropertiesService modelPropertiesService;

    protected abstract String getModelNameForError();

    public AbstractModelRequestService(final R entityRepository)
    {
        this.entityRepository = entityRepository;
    }

    protected abstract T getDtoFromModel(final M modelObject);

    protected List<T> getDtoListFromModels(final Page<M> modelCollection)
    {
        return modelCollection.stream().map(this::getDtoFromModel).collect(Collectors.toList());
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

        if (foundModels.getTotalElements() == 0) {
            log.info("FIND ALL | No objects of class {} found. Throwing NotFoundException.", getModelNameForError());
            throw new NotFoundException("Cannot find any objects.", getModelNameForError());
        }
        return getDtoListFromModels(foundModels);
    }

    @Override
    public T getSingleObject(final String code)
    {
        final Optional<M> foundModelObject = entityRepository.findByCode(code);
        if (foundModelObject.isEmpty()) {
            log.info("FIND SINGLE | No objects of class {} found. Throwing NotFoundException.", getModelNameForError());
            throw new NotFoundException("Cannot find object of given code.", getModelNameForError());
        }
        return getDtoFromModel(foundModelObject.get());
    }

    @Override
    public HttpStatus save(final Set<T> dto)
    {
        entityRepository.saveAll(getModelListFromDtos(dto));
        return HttpStatus.OK;
    }

    @Override
    public HttpStatus delete(final String code)
    {
        final Optional<M> foundModelObject = entityRepository.findByCode(code);
        if (foundModelObject.isEmpty()) {
            log.info("DELETE | No objects of class {} found. Throwing NotFoundException.", getModelNameForError());
            throw new NotFoundException("Cannot find object of given code.", getModelNameForError());
        }
        entityRepository.delete(foundModelObject.get());
        return HttpStatus.OK;
    }

    @Override
    public HttpStatus patch(final T dtoPatch, final String code)
    {
        final Optional<M> foundModelObject = entityRepository.findByCode(code);
        if (foundModelObject.isEmpty()) {
            log.info("PATCH | No objects of class {} found. Throwing NotFoundException.", getModelNameForError());
            throw new NotFoundException("Cannot find object of given code.", getModelNameForError());
        }
        modelPatchService.applyPatch(foundModelObject.get(),dtoPatch);
        entityRepository.save(foundModelObject.get());
        return HttpStatus.OK;
    }


}
