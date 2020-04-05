package pl.polsl.photoplus.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.polsl.photoplus.annotations.Patchable;
import pl.polsl.photoplus.services.controllers.exceptions.PatchException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service is responsible for patching model objects based on connected DTO object.
 * Initial requirements:
 * All field of model and dto class which can modified by API needs to be marked with annotation.
 *
 * @see pl.polsl.photoplus.annotations.Patchable
 * Name of field in model and DTO needs to be the same.
 * You can specify custom setter or getter in annotation. To do for example some extra stuff.
 */
@Service
@Slf4j
public class ModelPatchService
{
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * Method for applying patch on given objects.
     *
     * @param modelToPatch Model which values will be changed. Fields should be annotated. Method should be defined as setter.
     * @param modelDtoPatch Model which has changed value. Fields should be annotated. Method should be defined as getter.
     */
    public void applyPatch(final Object modelToPatch, final Object modelDtoPatch)
    {
        final Map<Field,Optional<Method>> modelMap = getFieldsWithMethods(getAnnotatedFieldsAndMakeAccessible(modelToPatch), modelToPatch
                .getClass());
        final Map<Field,Optional<Method>> modelDtoMap = getFieldsWithMethods(filterDtoFieldsPatch(modelDtoPatch), modelDtoPatch
                .getClass());
        final Set<Map.Entry<Field,Optional<Method>>> modelMapEntrySet = modelMap.entrySet();
        for (final var fieldPatchEntry : modelDtoMap.entrySet()) {
            final Field fieldPatch = fieldPatchEntry.getKey();
            final Optional<Method> methodToGet = fieldPatchEntry.getValue();

            final Map.Entry<Field,Optional<Method>> fieldToPatchEntry = getFieldRecordByName(fieldPatch.getName(), modelMapEntrySet);
            final Field fieldToPatch = fieldToPatchEntry.getKey();
            final Optional<Method> methodToSet = fieldToPatchEntry.getValue();

            final Object value = getFieldValue(fieldPatch, fieldToPatch, methodToGet, modelDtoPatch);
            setFieldValue(fieldToPatch, fieldPatch, methodToSet, modelToPatch, value);
        }
    }

    private List<Field> getAnnotatedFieldsAndMakeAccessible(final Object modelToPatch)
    {
        return setAccessible(Arrays.stream(modelToPatch.getClass().getDeclaredFields())
                                     .filter(field -> field.isAnnotationPresent(Patchable.class))
                                     .collect(Collectors.toList()));
    }

    private List<Field> setAccessible(final List<Field> fields)
    {
        fields.forEach(field -> field.setAccessible(true));
        return fields;
    }

    private Map<Field,Optional<Method>> getFieldsWithMethods(final List<Field> fields, final Class clazz)
    {

        return fields.stream()
                .collect(Collectors.toMap(Function.identity(), field -> extractMethodNameIfNotEmpty(field, clazz)));
    }

    private Optional<Method> extractMethodNameIfNotEmpty(final Field field, final Class clazz)
    {
        final String methodName = field.getAnnotation(Patchable.class).method();
        final Stream<Method> methods = Stream.of(clazz.getMethods());
        if (methodName.isBlank()) {
            return Optional.empty();
        }
        return methods.filter(method -> method.getName().equals(methodName)).findFirst();
    }

    private List<Field> filterDtoFieldsPatch(final Object modelDtoPatch)
    {
        final List<Field> dtoFields = getAnnotatedFieldsAndMakeAccessible(modelDtoPatch);
        final Predicate<Field> filteringPredicate = createFilteringPredicate(modelDtoPatch);

        return dtoFields.stream().filter(filteringPredicate).collect(Collectors.toList());
    }

    private Predicate<Field> createFilteringPredicate(final Object modelDtoPatch)
    {
        final Predicate<Field> fieldFilter = field -> {
            try {
                //remove null fields
                if (field.get(modelDtoPatch) == null) {
                    return false;
                }
                //if some constraints are violated
                final Set<? extends ConstraintViolation> violations = validator.validateValue(modelDtoPatch.getClass(), field
                        .getName(), field.get(modelDtoPatch));
                if (!violations.isEmpty()) {
                    final StringBuilder message = new StringBuilder();

                    message.append("Field: '");
                    message.append(field.getName());
                    message.append("' has validation errors: ");
                    violations.forEach(violation -> message.append(violation.getMessage() + " "));

                    throw new PatchException(message.toString(), ModelPatchService.class.getSimpleName());
                }
            } catch (final IllegalAccessException e) {
                log.warn(e.getMessage());
                return false;
            }
            return true;
        };
        return fieldFilter;
    }

    /**
     * Looks for map entry with given field name.
     */
    private Map.Entry<Field,Optional<Method>> getFieldRecordByName(final String name, final Set<Map.Entry<Field,Optional<Method>>> mapEntrySet)
    {
        final Optional<Map.Entry<Field,Optional<Method>>> entry = mapEntrySet.stream()
                .filter(mapEntry -> mapEntry.getKey().getName().equals(name))
                .findFirst();
        if (entry.isEmpty()) {
            log.error("Unable to find field from DTO: '{}' in fields of entity. Aborting patching process.", name);
            throw new PatchException("Internal error in patching service.", this.getClass().getSimpleName());
        }
        return entry.get();
    }

    /**
     * Method is responsible for getting new field value either directly from field or via method given in annotation.
     */
    private Object getFieldValue(final Field field, final Field fieldFromModel, final Optional<Method> method, final Object objectToInvoke)
    {
        final Object value;
        try {
            if (method.isPresent()) {
                if (!checkIfMethodIsGetter(fieldFromModel, method.get())) {
                    log.error("Method: '{}' for field: '{}' is not getter", method.get().getName(), field.getName());
                    throw new PatchException("Internal error in patching service.", this.getClass().getSimpleName());
                }
                value = method.get().invoke(objectToInvoke);
            } else {
                value = field.get(objectToInvoke);
            }
        } catch (final IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage());
            throw new PatchException("Internal error in patching service.", this.getClass().getSimpleName());
        }
        return value;
    }

    /**
     * Method is responsible for setting field value either directly from field or via method given in annotation.
     */
    private void setFieldValue(final Field field, final Field fieldFromDto, final Optional<Method> method, final Object objectToInvoke, final Object value)
    {
        try {
            if (method.isPresent()) {
                if (!checkIfMethodIsSetter(fieldFromDto, method.get())) {
                    log.error("Method: '{}' for field: '{}' is not setter", method.get().getName(), field.getName());
                    throw new PatchException("Internal error in patching service.", this.getClass().getSimpleName());
                }
                method.get().invoke(objectToInvoke, value);
            } else {
                field.set(objectToInvoke, value);
            }
        } catch (final IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage());
            throw new PatchException("Internal error in patching service.", this.getClass().getSimpleName());
        }

    }

    /**
     * Checks if method given in annotation is setter.
     * It should has only one parameter of type of field and return void.
     */
    private boolean checkIfMethodIsSetter(final Field fieldFromDto, final Method methodToSet)
    {
        if (methodToSet.getReturnType().equals(Void.TYPE)) {
            final List<Class> parameters = List.of(methodToSet.getParameterTypes());
            if (parameters.size() == 1) {
                return parameters.get(0).equals(fieldFromDto.getType());
            }
        }
        return false;
    }

    /**
     * Checks if method given in annotation is getter.
     * It should not have parameters and return parameter which is type of field.
     */
    private boolean checkIfMethodIsGetter(final Field fieldFromModel, final Method methodToSet)
    {
        if (methodToSet.getReturnType().equals(fieldFromModel.getType())) {
            final List<Class> parameters = List.of(methodToSet.getParameterTypes());
            return parameters.size() == 0;
        }
        return false;
    }

}
