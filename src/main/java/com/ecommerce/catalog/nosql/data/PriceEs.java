package com.ecommerce.catalog.nosql.data;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@RequiredArgsConstructor
public class PriceEs implements Serializable {

    private Integer id;
    private Boolean currentPrice;
    private BigDecimal price;
    private String reference;

    private Integer customerGroupId;

    // Currency
    private Integer currencyId;
    private String currencySymbol;
    private String currencyLabel;

}
