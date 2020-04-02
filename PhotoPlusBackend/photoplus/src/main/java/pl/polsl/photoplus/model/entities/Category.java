package pl.polsl.photoplus.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.polsl.photoplus.annotations.Patchable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "categories")
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
public class Category extends AbstractEntityModel {

    public Category(final String name) {
        this.name = name;
    }

    @Patchable
    private String name;

}
