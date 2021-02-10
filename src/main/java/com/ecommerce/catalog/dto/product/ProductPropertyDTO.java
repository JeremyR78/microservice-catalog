package com.ecommerce.catalog.dto.product;

import com.ecommerce.catalog.dto.property.PropertyDTO;
import com.ecommerce.catalog.dto.property.PropertyValueDTO;
import com.ecommerce.catalog.model.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPropertyDTO implements Serializable  {

    private PropertyDTO property;
    private List<PropertyValueDTO> propertyValues;

}
