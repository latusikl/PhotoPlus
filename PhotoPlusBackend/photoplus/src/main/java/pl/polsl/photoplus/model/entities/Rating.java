package pl.polsl.photoplus.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.polsl.photoplus.annotations.Patchable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity(name = "ratings")
@Table(name = "ratings")
@Getter
@Setter
@NoArgsConstructor
public class Rating extends AbstractEntityModel {

    public Rating(final Integer rate, final String content, final LocalDate date) {
        this.rate = rate;
        this.content = content;
        this.date = date;
    }

    @ManyToOne
    private User user;

    @ManyToOne
    private Product product;

    @Patchable
    private Integer rate;

    @Patchable
    private String content;

    @Patchable
    private LocalDate date;
}
