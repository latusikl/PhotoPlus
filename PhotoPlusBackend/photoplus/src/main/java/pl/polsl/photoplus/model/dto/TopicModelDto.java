package pl.polsl.photoplus.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import pl.polsl.photoplus.annotations.Patchable;
import pl.polsl.photoplus.components.ContextProvider;
import pl.polsl.photoplus.model.entities.Section;
import pl.polsl.photoplus.services.controllers.SectionService;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
public class TopicModelDto
        extends AbstractModelDto
{
    @NotBlank(message = "Name is mandatory.")
    @JsonProperty("name")
    @Patchable
    private String name;

    @NotBlank(message = "Section code is mandatory.")
    @JsonProperty("sectionCode")
    @Patchable(method = "sectionPatch")
    private String section;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "Date is mandatory.")
    @JsonProperty("date")
    @Patchable
    private LocalDate date;

    @NotBlank(message = "User code is mandatory.")
    @JsonProperty("userCode")
    private String userCode;

    @JsonProperty("userLogin")
    private String userLogin;

    public TopicModelDto(final String code, final String name, final String section, final LocalDate date,
                         final String userCode, final String userLogin)
    {
        super(code);
        this.section = section;
        this.date = date;
        this.userCode = userCode;
        this.name = name;
        this.userLogin = userLogin;
    }

    @JsonIgnore
    public Section sectionPatch()
    {
        final SectionService sectionService = ContextProvider.getBean(SectionService.class);
        return sectionService.findByCodeOrThrowError(section, "TopicPatchSection");
    }

}
