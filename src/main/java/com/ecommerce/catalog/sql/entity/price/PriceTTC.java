package com.ecommerce.catalog.sql.entity.price;

import com.ecommerce.catalog.sql.entity.tax.Tax;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "price_ttc")
@DiscriminatorValue( "TTC")
public class PriceTTC extends Price {

    @ManyToOne
    @JoinColumn(name="fk_id_price_ht")
    private Price priceHt;

    @ManyToOne
    @JoinColumn(name="fk_id_tax")
    private Tax tax;


}
