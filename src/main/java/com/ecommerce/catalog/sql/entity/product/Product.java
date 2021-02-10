package com.ecommerce.catalog.sql.entity.product;

import com.ecommerce.catalog.model.PriceType;
import com.ecommerce.catalog.sql.entity.category.CategoryProductLink;
import com.ecommerce.catalog.sql.entity.common.ImageData;
import com.ecommerce.catalog.sql.entity.price.Currency;
import com.ecommerce.catalog.sql.entity.price.Price;
import com.ecommerce.catalog.sql.entity.property.Property;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name="product")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_id_product", nullable = false)
    private Integer id;

    private String reference;

    //
    //  PRICES
    //

    /**
     * All prices like price HT, price TVA and price PROMO
     *
     */
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL )
    private List<Price> prices;

    //
    //  TRANSLATIONS
    //

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL )
    private List<ProductTranslation> productTranslations;

    //
    //  CATEGORIES
    //

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL )
    private List<CategoryProductLink> categoryProductLinks;

    @JoinTable(
            name = "product_dimension_link",
            joinColumns = { @JoinColumn(name = "pfk_id_dimension") },
            inverseJoinColumns = { @JoinColumn(name = "pfk_id_product") }
    )
    @ManyToMany
    private List<Dimension> dimensions;

    //
    //  IMAGES
    //

    @JoinTable(
            name = "product_image_link",
            joinColumns = { @JoinColumn(name = "pfk_id_image") },
            inverseJoinColumns = { @JoinColumn(name = "pfk_id_product") }
    )
    @ManyToMany
    private List<ImageData> images;

    @JoinTable(
            name = "product_property_link",
            joinColumns = { @JoinColumn(name = "pfk_id_product") },
            inverseJoinColumns = { @JoinColumn(name = "pfk_id_property") }
    )
    @ManyToMany
    private List<Property> tagList;

    //
    //  PROPERTIES
    //

    @OneToMany(mappedBy = "product" , cascade = CascadeType.ALL )
    private List<ProductProperty> propertyProductList;

    /**
     * Link several products with only 1 main property value different
     */
    @ManyToOne
    @JoinColumn( name="fk_id_product_link" )
    private ProductLink productLink;

    // --------------------------------------
    // -            Methods                 -
    // --------------------------------------

    /**
     * Récupére le dernier prix du produit dans le monaie demandée.
     *
     *
     * @param currency : La monaie
     * @param type : Le type ( TTC, HT, PROMO )
     * @return
     */
    public Price getPriceForCurrency( Currency currency, PriceType type ){
        List<Price> prices = this.getPrices();
        for( Price price : prices )
        {
            if( Objects.equals( price.getCurrency(), currency )
                    && Objects.equals( type, price.getPriceType() )
                    && price.getCurrentPrice() )
            {
                return price;
            }
        }
        return null;
    }

    /**
     * Récupére la liste de tous les prix pour cette monaie
     *
     * @param currency
     * @param type : Le type ( TTC, HT, PROMO )
     * @return
     */
    public List<Price> getAllPriceForCurrency( Currency currency,  PriceType type ){
        List<Price> pricesCurrency = new ArrayList<>();
        List<Price> prices = this.getPrices();
        for( Price price : prices )
        {
            if( Objects.equals( price.getCurrency(), currency )
                    && Objects.equals( type, price.getPriceType() ) )
            {
                pricesCurrency.add( price );
            }
        }
        return pricesCurrency;
    }

    /**
     * Récupération de la meilleur promotion
     *
     * @return
     */
//    public Promotion getBestPromotion(Date date )
//    {
//        Promotion bestPromotion = null;
//        List<Promotion> promotionList = this.getPromotionList();
//
//        for( Promotion promotion : promotionList )
//        {
//            if( promotion.isEnablePromotion( date ) )
//            {
//                if (bestPromotion == null) {
//                    bestPromotion = promotion;
//                }
//                else if ( promotion.getPriceWithPromotion().getPrice().compareTo( bestPromotion.getPriceWithPromotion().getPrice()) < 0 ) {
//                    bestPromotion = promotion;
//                }
//            }
//        }
//        return bestPromotion;
//    }


    @Override
    public final String toString() {
        return "Product{" +
                "id=" + id +
                ", reference='" + reference + '\'' +
                '}';
    }
}
