package pl.polsl.photoplus.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.polsl.photoplus.services.annotations.Patchable;
import pl.polsl.photoplus.services.controllers.exceptions.PatchException;

import javax.annotation.PostConstruct;
import javax.validation.Validation;
import javax.validation.Validator;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Service is responsible for patching model objects based on connected DTO object.
 * Initial requirements:
 * All field of Model class which can modified by API needs to be marked with annotation.
 *
 * @see pl.polsl.photoplus.services.annotations.Patchable
 * Name of field in model and DTO needs to be the same.
 * Getters and setters for fields are reqiured.
 */
@Service
@Slf4j
public class ModelPatchService
{
    protected static final List<String> BLACKLISTED_METHOD_LIST = new ArrayList<>();

    private static final String GETTER_PREFIX = "get";

    private static final String SETTER_PREFIX = "set";

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @PostConstruct
    protected void addBlackListedMethodsNames()
    {
        BLACKLISTED_METHOD_LIST.add("getClass");
        BLACKLISTED_METHOD_LIST.add("getCode");
    }

    public void applyPatch(final Object modelToPatch, final Object modelDtoPatch)
    {
        final List<Method> filteredDtoGetters = filterGettersForPatch(modelDtoPatch);
        final List<Method> filteredModelSetters = filterSettersForPatch(filteredDtoGetters, modelToPatch);
        for (final var setter : filteredModelSetters) {
            final Optional<Method> getter = getGetterForSetter(filteredDtoGetters, setter);
            if (getter.isEmpty()) {
                log.warn("Unable to find getter for setter: {}", setter.getName());
                return;
            }
            try {
                setter.invoke(modelToPatch, getter.get().invoke(modelDtoPatch));
            } catch (final IllegalAccessException | InvocationTargetException e) {
                log.warn("Unable to properly apply patch to model object.");

            }
        }
    }

    private Optional<Method> getGetterForSetter(final List<Method> getters, final Method setter)
    {
        return getters.stream()
                .filter(getter -> getPropertyNameFromGetter(getter.getName()).equals(getPropertyNameFromSetter(setter.getName())))
                .findFirst();
    }

    private List<Method> filterGettersForPatch(final Object modelDtoPatch)
    {
        final Stream<Method> methodStream = getObjectMethods(modelDtoPatch).stream();

        Predicate<Method> nonGettersAndBlacklistedPredicate = method -> {
            String methodName = method.getName();
            if (methodName.startsWith(GETTER_PREFIX)) {
                if (BLACKLISTED_METHOD_LIST.contains(methodName)) {
                    log.info("Blacklisted method {} removed from list.", methodName);
                    return false;
                }
                return true;
            }
            return false;
        };

        final Predicate<Method> validationPredicate = getter -> {
            try {
                return getter != null && validator.validateValue(modelDtoPatch.getClass(), getPropertyNameFromGetter(getter.getName()), getter
                        .invoke(modelDtoPatch)).isEmpty();
            } catch (final InvocationTargetException | IllegalAccessException e) {
                log.warn("Error in ModelPatchService: \n {}", e.getMessage());
                throw new PatchException("Patching object failure.", "Patcher");
            }
        };

        return methodStream.filter(nonGettersAndBlacklistedPredicate).
                filter(validationPredicate).collect(Collectors.toList());

    }

    private List<Method> filterSettersForPatch(final List<Method> getters, final Object modelToPatch)
    {
        final List<String> annotatedFieldsNames = getAnnotatedFields(modelToPatch).stream()
                .map(Field::getName)
                .collect(Collectors.toList());
        final List<String> updatedPropertiesNames = getPropertyNameListFromGetters(getters);
        final List<Method> methods = getObjectMethods(modelToPatch);
        return methods.stream()
                //Setters
                .filter(method -> method.getName().startsWith(SETTER_PREFIX))
                //Setters of annotated fields
                .filter(method -> annotatedFieldsNames.contains(getPropertyNameFromSetter(method.getName())))
                //Setters of changed fields
                .filter(method -> updatedPropertiesNames.contains(getPropertyNameFromSetter(method.getName())))
                .collect(Collectors.toList());
    }

    private List<String> getPropertyNameListFromGetters(List<Method> getters)
    {
        return getters.stream().map(method -> getPropertyNameFromGetter(method.getName())).collect(Collectors.toList());
    }

    private List<Field> getAnnotatedFields(final Object modelToPatch)
    {
        return Arrays.stream(modelToPatch.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Patchable.class))
                .collect(Collectors.toList());
    }

    private List<Method> getObjectMethods(final Object object)
    {
        return Arrays.asList(object.getClass().getMethods());
    }

    protected String getPropertyNameFromSetter(final String methodName)
    {
        return lowerFirstLetter(methodName.substring(SETTER_PREFIX.length()));
    }

    private String getPropertyNameFromGetter(final String methodName)
    {
        return lowerFirstLetter(methodName.substring(GETTER_PREFIX.length()));
    }

    private String lowerFirstLetter(final String propertyName)
    {
        final StringBuilder stringBuilder = new StringBuilder();
        if (propertyName.length() > 0) {
            stringBuilder.append(propertyName.toLowerCase().charAt(0));
            stringBuilder.append(propertyName.substring(1));
        }
        return stringBuilder.toString();
    }

}
