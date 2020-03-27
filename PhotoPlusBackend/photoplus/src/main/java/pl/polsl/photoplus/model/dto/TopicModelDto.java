package pl.polsl.photoplus.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;

@Getter
@Setter
public class TopicModelDto extends AbstractModelDto  {
    public TopicModelDto(final String code, final String sectionCode, final SimpleDateFormat date,
                         final String userCode) {
        super(code);
        this.sectionCode = sectionCode;
        this.date = date;
        this.userCode = userCode;
    }

    @NotBlank(message = "Name is mandatory.")
    private String name;

    @NotBlank(message = "Section code is mandatory.")
    private String sectionCode;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "Date is mandatory.")
    private SimpleDateFormat date;

    @NotBlank(message = "User code is mandatory.")
    private String userCode;
}
