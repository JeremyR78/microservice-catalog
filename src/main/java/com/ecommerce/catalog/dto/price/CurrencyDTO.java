package com.ecommerce.catalog.dto.price;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyDTO implements Serializable {

    private Integer id;
    private String symbol;
    private String label;

}
