package com.ecommerce.catalog.dto.price;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceView implements Serializable {

    private BigDecimal price;
    private String symbol;

    //private BigInteger publicPrice;

}
