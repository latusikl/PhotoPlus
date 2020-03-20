package pl.polsl.photoplus.controllers.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.polsl.photoplus.model.dto.ErrorDto;
import pl.polsl.photoplus.services.controllers.exceptions.NotFoundException;
import pl.polsl.photoplus.services.controllers.exceptions.PatchException;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Global exception handler for exceptions thrown among all Controllers.
 *
 * @see ControllerAdvice
 */
@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler
{
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<List<ErrorDto>> handleValidationException(final MethodArgumentNotValidException e)
    {

        log.info("Validation exception handled: {}", e.getMessage());
        final List<ErrorDto> errorDtos = new ArrayList<>();

        final Consumer<ObjectError> errorConsumer = objectError -> {
            final String message = objectError.getDefaultMessage();
            final String error = "Field: " + ((FieldError)objectError).getField();
            errorDtos.add(new ErrorDto(MethodArgumentNotValidException.class.getSimpleName(),error, message));
        };

        e.getBindingResult().getAllErrors().forEach(errorConsumer);

        return new ResponseEntity<>(errorDtos,HttpStatus.EXPECTATION_FAILED);
    }


    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<ErrorDto> handleNotFoundException(final NotFoundException e)
    {
        log.info("NotFoundException handled for type: {}.", e.getCauseClassType());

        final ErrorDto error = new ErrorDto(NotFoundException.class.getSimpleName(),e.getCauseClassType(), e.getMessage());
        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ErrorDto> constraintViolationException(final NotFoundException e)
    {
        log.info("ConstraintViolationException handled for type: {}.", e.getCauseClassType());

        final ErrorDto error = new ErrorDto(NotFoundException.class.getSimpleName(),e.getCauseClassType(), e.getMessage());
        return new ResponseEntity<>(error,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PatchException.class)
    protected ResponseEntity<ErrorDto> constraintViolationException(final PatchException e)
    {
        log.info("ConstraintViolationException handled for type: {}.", e.getCauseClassType());

        final ErrorDto error = new ErrorDto(PatchException.class.getSimpleName(),e.getCauseClassType(), e.getMessage());
        return new ResponseEntity<>(error,HttpStatus.EXPECTATION_FAILED);
    }
}
