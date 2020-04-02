package pl.polsl.photoplus.model.dto;

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
    private LocalDate date;

    @NotBlank(message = "Topic code is mandatory.")
    private String topicCode;

    @NotBlank(message = "Content is mandatory.")
    private String content;
}
