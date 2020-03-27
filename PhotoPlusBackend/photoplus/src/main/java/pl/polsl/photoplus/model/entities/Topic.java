package pl.polsl.photoplus.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import pl.polsl.photoplus.annotations.Patchable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.text.SimpleDateFormat;

@Entity(name = "topics")
@Table(name = "topics")
@Getter
@Setter
@NoArgsConstructor
public class Topic extends AbstractEntityModel {

    public Topic(final String name, final SimpleDateFormat date) {
        this.name = name;
        this.date = date;
    }

    @Patchable
    private String name;

    @ManyToOne
    @Patchable
    private Section section;

    @Patchable
    private SimpleDateFormat date;

    @ManyToOne
    private User creator;
}
