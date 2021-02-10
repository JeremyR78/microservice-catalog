package com.ecommerce.catalog.sql.entity.price;


import com.ecommerce.catalog.sql.entity.promotion.Promotion;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "price_promo")
@DiscriminatorValue( "PROMO")
public class PricePromo extends Price {

    @ManyToOne
    @JoinColumn(name="fk_id_price_initial")
    private Price priceInitial;

    @ManyToOne
    @JoinColumn(name="fk_id_promotion")
    private Promotion promotion;

    private Boolean enable;

    // --------------------------------------
    // -            Methods                 -
    // --------------------------------------



    /**
     * Calcul le pourcentage de réduction
     *
     * @return
     */
    public Double getPercentage(){
        if( this.getPrice() != null  && this.getPriceInitial() != null && this.getPriceInitial().getPrice() != null  ){
            return ( this.getPrice().doubleValue() * 100 / this.getPriceInitial().getPrice().doubleValue() ) - 100 ;
        }
        return null;
    }


    /**
     *
     * @param currentDate
     * @return
     */
    public boolean isEnablePromotion( Date currentDate ){
        return this.getPromotion().isEnablePromotion( currentDate )
                && this.getEnable()
                && this.getPriceInitial().getCurrentPrice()
                && this.getCurrentPrice();
    }




}
