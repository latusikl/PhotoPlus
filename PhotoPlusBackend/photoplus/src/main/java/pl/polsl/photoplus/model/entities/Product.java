package pl.polsl.photoplus.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.polsl.photoplus.annotations.Patchable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity(name = "products")
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product extends AbstractEntityModel {

    public Product(final String name, final Integer price, final String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    @Patchable
    private String name;

    @ManyToOne
    @Patchable
    private Category category;

    @Patchable
    private Integer price;

    @Patchable
    private String description;

}