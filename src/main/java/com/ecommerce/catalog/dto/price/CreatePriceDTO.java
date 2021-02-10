package com.ecommerce.catalog.dto.price;

import com.ecommerce.catalog.model.PriceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePriceDTO implements Serializable {

    private BigDecimal price;
    private Integer currencyId;
    private Integer productId;
    private PriceType priceType;
    private Integer storeId;
    private Integer customerGroupId;
    private Boolean currentPrice;

    // PROMOTION
    private Integer priceInitialId;
    private Integer promotionId;
    private Boolean enable;

    // TTC
    private Integer priceHtId;
    private Integer taxId;

}
