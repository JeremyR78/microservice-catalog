package com.ecommerce.catalog.nosql.data;

import com.ecommerce.catalog.model.PropertyType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class PropertyEs implements Serializable {

    private int id;
    private boolean enable;
    private String label;
    private List<String> values;

    // Property unit
    private Integer propertyUnitId;
    private PropertyType propertyType;
    private String propertyUnitLabel;

}
