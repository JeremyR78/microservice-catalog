package com.ecommerce.catalog.dto.property;

import com.ecommerce.catalog.model.PropertyType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyUnitDTO implements Serializable {

    private Integer id;

    @JsonProperty(value = "property_type")
    private PropertyType propertyType;

    @JsonProperty(value = "translations")
    private List<PropertyUnitTranslationDTO> propertyUnitTranslationList;

}
