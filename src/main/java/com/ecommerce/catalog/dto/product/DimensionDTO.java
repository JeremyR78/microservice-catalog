package com.ecommerce.catalog.dto.product;

import com.ecommerce.catalog.model.DimensionType;
import com.ecommerce.catalog.sql.entity.property.Property;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DimensionDTO {

    private Integer id;
    private DimensionType dimensionType;
    private List<Property> propertiesDimension;

}
