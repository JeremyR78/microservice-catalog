package com.ecommerce.catalog.dto.promotion;

import com.ecommerce.catalog.model.PriceUnitType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PromoCodeDTO implements Serializable {

    private Integer id;
    private Boolean enable;
    private String code;
    private BigDecimal discount;
    private Integer currencyId;
    private PriceUnitType priceUnitType;
    private Integer storeId;
    private Integer countryId;
    private Integer customerGroupId;
    private Date startAt;
    private Date finishAt;

}
