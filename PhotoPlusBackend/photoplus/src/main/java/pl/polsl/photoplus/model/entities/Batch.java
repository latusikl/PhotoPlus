package pl.polsl.photoplus.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.polsl.photoplus.annotations.Patchable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity(name = "batches")
@Table(name = "batches")
@Getter
@Setter
@NoArgsConstructor
public class Batch
        extends AbstractEntityModel
{

    @ManyToOne
    private Product product;

    @Patchable
    private Double purchasePrice;

    @Patchable
    private LocalDate date;

    @Patchable
    private Integer supplyQuantity;

    @Patchable
    private Integer storeQuantity;

    public Batch(final Double purchasePrice, final LocalDate date, final Integer supplyQuantity, final Integer storeQuantity)
    {
        this.purchasePrice = purchasePrice;
        this.date = date;
        this.supplyQuantity = supplyQuantity;
        this.storeQuantity = storeQuantity;
    }

}
