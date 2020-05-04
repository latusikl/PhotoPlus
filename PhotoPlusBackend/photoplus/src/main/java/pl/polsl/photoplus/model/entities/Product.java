package pl.polsl.photoplus.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import pl.polsl.photoplus.annotations.Patchable;

import javax.persistence.*;
import java.util.List;

@Entity(name = "products")
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product extends AbstractEntityModel {

    public Product(final String name, final Double price, final String description, final Integer storeQuantity,
                   final List<String> imageCodes) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageCodes = imageCodes;
        this.storeQuantity = storeQuantity;
    }

    @Patchable
    private String name;

    @ManyToOne
    @Patchable
    private Category category;

    @Patchable
    private Double price;

    @Patchable
    @Column(columnDefinition = "text")
    private String description;

    @Patchable
    @ColumnDefault("0")
    private Integer storeQuantity;

    @Patchable
    @ElementCollection
    private List<String> imageCodes;

}