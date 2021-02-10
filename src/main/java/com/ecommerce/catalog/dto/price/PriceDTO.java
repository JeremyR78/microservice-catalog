package com.ecommerce.catalog.dto.price;

import com.ecommerce.catalog.model.PriceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceDTO implements Serializable {

    private Integer id;
    private BigDecimal price;
    private Boolean currentPrice;
    private CurrencyDTO currency;
    private PriceType priceType;
    private Integer storeId;
    private Integer productId;
    private Integer customerGroupId;

    // PROMOTION
    private Integer promotionId;
    private Integer priceInitialId;
    private Boolean enable;

    // TTC
    private Integer priceHtId;
    private Integer taxId;


}
