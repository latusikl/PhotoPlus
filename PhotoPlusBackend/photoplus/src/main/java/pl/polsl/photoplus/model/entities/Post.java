package pl.polsl.photoplus.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.text.SimpleDateFormat;

@Entity(name = "posts")
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
public class Post extends AbstractEntityModel {

    public Post(final SimpleDateFormat date) {
        this.date = date;
    }

    @ManyToOne
    private Topic topic;

    private SimpleDateFormat date;
}
