package pl.polsl.photoplus.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;

@Getter
@Setter
public class PostModelDto extends AbstractModelDto<PostModelDto> {
    public PostModelDto(final String code, final SimpleDateFormat date, final String topicCode) {
        super(code);
        this.date = date;
        this.topicCode = topicCode;
    }

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "Date is mandatory.")
    private SimpleDateFormat date;

    @NotBlank(message = "Topic code is mandatory.")
    private String topicCode;
}
