package com.ecommerce.catalog.dto.product;

import com.ecommerce.catalog.dto.property.PropertyView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductView implements Serializable {

    private Integer id;
    private String reference;

    //
    // PRICE
    //

    private String symbolPrice;

    // - PUBLIC
    // TTC
    private BigDecimal pricePublicTTC;
    // HT
    private BigDecimal pricePublicHT;
    // PROMO
    private Double pricePublicPromotionPercent;
    private BigDecimal pricePublicWithPromo;
    private Date startPromoAt;
    private Date finishPromoAt;


    //
    // DESCRIPTION
    //
    private String model;
    private String description;
    private String presentation;

    //
    // PROPERTIES
    //
    private String dimensionProperty;
    private List<PropertyView> tagList;
    private List<ProductPropertyView> productPropertyViews;
}
