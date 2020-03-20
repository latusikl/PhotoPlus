package pl.polsl.photoplus.controllers.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pl.polsl.photoplus.model.dto.AbstractModelDto;
import pl.polsl.photoplus.services.controllers.ModelRequestService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * Basic controller for model object responsible for handling standard operations on objects.
 * Handled operations should be provided in equivalent service which implements given interface.
 *
 * @param <T> Typ of DTO for given controller.
 *  There needs to be created dtoService for given object to make it work.
 * @see pl.polsl.photoplus.services.controllers.ModelRequestService
 */
public abstract class BaseModelController<T extends AbstractModelDto>
{
    /**
     * Service needs to be injected manually by calling super class constructor
     */
    private ModelRequestService<T> dtoService;

    BaseModelController(final ModelRequestService dtoService){
        this.dtoService=dtoService;
    }

    @GetMapping("all/{page}")
    public ResponseEntity<List<T>> getAll(@PathVariable("page") final Integer page)
    {
        return new ResponseEntity<>(dtoService.getPageFromAll(page), HttpStatus.OK);
    }

    @GetMapping("/{code}")
    public ResponseEntity<T> getSingle(@PathVariable("code") final String page)
    {
        return new ResponseEntity<>(dtoService.getSingleObject(page), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity post(@RequestBody @Valid final Set<T> dtoSet)
    {
        return new ResponseEntity(dtoService.save(dtoSet));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity delete(@PathVariable("code") final String code)
    {
        return new ResponseEntity(dtoService.delete(code));
    }

    @PatchMapping("/{code}")
    public ResponseEntity patch(@RequestBody final T dtoPatch, @PathVariable("code") final String code)
    {
        return new ResponseEntity(dtoService.patch(dtoPatch, code));
    }
}
