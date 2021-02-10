package com.ecommerce.catalog.dto.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDTO implements Serializable {

    private Integer id;
    private PropertyUnitDTO propertyUnit;
    private boolean enable;
    private List<PropertyTranslationDTO> translations;
    private List<PropertyValueDTO> propertyValues;

}
