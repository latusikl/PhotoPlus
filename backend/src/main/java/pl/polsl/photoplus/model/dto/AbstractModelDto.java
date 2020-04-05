package pl.polsl.photoplus.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

/**
 * Base class for all model DTOs.
 * Contains common property which is object code.
 * Code should be generated for each Model object. It will allow to easily identify object via API not showing database related ID.
 * Code is ReadOnly and should not be changed via API.
 */
@Getter
@Setter
public abstract class AbstractModelDto<T extends RepresentationModel<? extends T>>
        extends RepresentationModel<T>
{
    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "code")
    private String code;

    public AbstractModelDto(final String code)
    {
        this.code = code;
    }

}
