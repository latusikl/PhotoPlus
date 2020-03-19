package pl.polsl.photoplus.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Base class for all DTOs.
 * Contains common property which is object code.
 * Code should be generated for each Model object. It will allow to easily identify object via API not showing database related ID.
 * Code is ReadOnly and should not be changed via API.
 */
@Data
public abstract class AbstractDto
{
    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "code")
    private String code;
}
