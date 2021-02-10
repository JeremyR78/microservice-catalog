package com.ecommerce.catalog.dto.price;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceCurrenciesDTO implements Serializable  {

    private Integer id;
    private Integer storeId;
    private Integer customerGroupId;
    private List<PriceDTO> priceList;

}
