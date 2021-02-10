package com.ecommerce.catalog.dto.product;

import com.ecommerce.catalog.dto.price.PriceDTO;
import com.ecommerce.catalog.dto.property.PropertyDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO implements Serializable {

    private Integer id;
    private String reference;
    private List<PriceDTO> prices;
    private List<DimensionDTO> dimensions;
    private List<PropertyDTO> tagList;
    private List<ProductPropertyDTO> propertyProductList;

}
