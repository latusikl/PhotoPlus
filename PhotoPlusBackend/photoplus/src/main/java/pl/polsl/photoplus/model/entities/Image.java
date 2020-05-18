package pl.polsl.photoplus.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import pl.polsl.photoplus.annotations.Patchable;

import javax.persistence.*;

@Entity(name = "images")
@Table(name = "images")
@Getter
@Setter
@NoArgsConstructor
public class Image extends AbstractEntityModel {

    public Image(final String name, final byte[] bytes) {
        this.name = name;
        this.bytes = bytes;
    }

    @Patchable
    private String name;

    @Patchable
    @Lob
    private byte[] bytes;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

}
