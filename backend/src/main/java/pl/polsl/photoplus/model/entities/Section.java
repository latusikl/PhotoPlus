package pl.polsl.photoplus.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.polsl.photoplus.annotations.Patchable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "sections")
@Table(name = "sections")
@Getter
@Setter
@NoArgsConstructor
public class Section extends AbstractEntityModel {
    public Section(final String name, final String description) {
        this.name = name;
        this.description = description;
    }

    @Patchable
    private String name;

    @Patchable
    private String description;
}
