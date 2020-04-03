package pl.polsl.photoplus.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class PostModelDto extends AbstractModelDto<PostModelDto> {
    public PostModelDto(final String code, final LocalDate date, final String topicCode, final String content) {
        super(code);
        this.date = date;
        this.topicCode = topicCode;
        this.content = content;
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "Date is mandatory.")
    @JsonProperty("date")
    private LocalDate date;

    @NotBlank(message = "Topic code is mandatory.")
    @JsonProperty("topicCode")
    private String topicCode;

    @NotBlank(message = "Content is mandatory.")
    @JsonProperty("content")
    private String content;
}
