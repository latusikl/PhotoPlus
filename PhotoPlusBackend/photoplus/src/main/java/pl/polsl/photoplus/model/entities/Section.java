package pl.polsl.photoplus.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import pl.polsl.photoplus.annotations.Patchable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "sections")
@Table(name = "sections")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Section extends AbstractEntityModel {

    @Patchable
    private String name;

    @Patchable
    private String description;
}
