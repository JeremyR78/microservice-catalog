package com.ecommerce.catalog.sql.entity.price;

import com.ecommerce.catalog.model.PriceType;
import com.ecommerce.catalog.sql.entity.common.Store;
import com.ecommerce.catalog.sql.entity.customer.CustomerGroup;
import com.ecommerce.catalog.sql.entity.product.Product;
import com.ecommerce.catalog.sql.entity.user.UserAction;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Un prix ne pas pas être modifier
 *
 * Cela permet de garder un historique
 *
 */
@Data
@Entity
@Table(name = "price")
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="price_type", discriminatorType=DiscriminatorType.STRING)
@DiscriminatorValue( "HT")
public class Price implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_id_price", nullable = false)
    private Integer id;

    /**
     * Le prix actuelle si "Vrai" à utiliser
     */
    @Column(name = "current_price")
    private Boolean currentPrice;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "fk_id_currency")
    private Currency currency;

    @ManyToOne
    @JoinColumn(name="fk_id_product")
    private Product product;

    /**
     * La boutique
     */
    @ManyToOne
    @JoinColumn(name="fk_id_store")
    private Store store;

    /**
     * Type de tarif : PUBLIC - PRO - PREMIUM
     */
    @ManyToOne
    @JoinColumn(name="fk_id_customer_group")
    private CustomerGroup customerGroup;

    /**
     * Date de création
      */
    @ManyToOne
    @JoinColumn(name = "fk_id_user_action")
    private UserAction userAction;


    public PriceType getPriceType(){
        if( this instanceof PriceTTC ){
            return  PriceType.TTC;
        }
        else if ( this instanceof PricePromo ){
            return  PriceType.PROMO;
        }
        else {
            return  PriceType.HT;
        }
    }


    @Override
    public String toString() {
        return "Price{" +
                "id=" + id +
                ", currentPrice=" + currentPrice +
                ", price=" + price +
                ", currency=" + currency +
                '}';
    }
}
