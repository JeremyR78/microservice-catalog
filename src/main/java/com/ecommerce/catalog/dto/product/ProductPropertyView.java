package com.ecommerce.catalog.dto.product;

import com.ecommerce.catalog.dto.property.PropertyView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPropertyView implements Serializable  {

    private PropertyView propertyView;
    private String value;

}
