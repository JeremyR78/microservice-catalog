package com.ecommerce.catalog.dto.product;

import com.ecommerce.catalog.dto.price.CreatePriceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProduct implements Serializable {

    private String reference;
    private List<CreatePriceDTO> prices;
    private List<Integer> dimensionsId;
    private List<Integer> tagIdList;
    private List<CreateProductProperty> propertyProductList;

}
