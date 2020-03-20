package pl.polsl.photoplus.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@JsonPropertyOrder({"error","object", "message"})
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class ErrorDto
{
    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "error")
    private String error;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "object")
    private String object;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "message")
    private String message;

    public ErrorDto(final String error,final String object, final String message)
    {
        this.error=error;
        this.object=object;
        this.message=message;
    }
}
