package pl.polsl.photoplus.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.polsl.photoplus.annotations.Patchable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

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


}
