package pl.polsl.photoplus.model.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.text.SimpleDateFormat;

public class TopicModelDto extends AbstractModelDto  {
    public TopicModelDto(final String code) {
        super(code);
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
