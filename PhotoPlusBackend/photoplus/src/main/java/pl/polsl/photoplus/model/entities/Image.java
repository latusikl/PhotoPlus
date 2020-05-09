package pl.polsl.photoplus.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import pl.polsl.photoplus.annotations.Patchable;

import javax.persistence.*;
import java.util.List;

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
    @Column(unique = true)
    private String name;

    @Patchable
    @Lob
    private byte[] bytes;

    @ManyToMany(mappedBy = "images", targetEntity = Product.class)
    private List<Product> products;

}
