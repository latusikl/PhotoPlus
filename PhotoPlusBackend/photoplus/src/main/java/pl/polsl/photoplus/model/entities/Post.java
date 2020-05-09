package pl.polsl.photoplus.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import pl.polsl.photoplus.annotations.Patchable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity(name = "posts")
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
public class Post extends AbstractEntityModel {

    public Post(final LocalDate date, final String content) {
        this.content = content;
        this.date = date;
    }

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Topic topic;

    private LocalDate date;

    @Patchable
    private String content;

    @ManyToOne
    private User user;
}
