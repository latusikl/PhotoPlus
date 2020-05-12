package pl.polsl.photoplus.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import pl.polsl.photoplus.annotations.Patchable;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity(name = "products")
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product extends AbstractEntityModel {

    public Product(final String name, final Double price, final String description, final Integer storeQuantity,
                   final Map<String, String> dataLinks) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.storeQuantity = storeQuantity;
        this.dataLinks = dataLinks;
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
    @ManyToMany(targetEntity = Image.class)
    private List<Image> images;

    @Patchable
    @ElementCollection
    private Map<String, String> dataLinks;

}