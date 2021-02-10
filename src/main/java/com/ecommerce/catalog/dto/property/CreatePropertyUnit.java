package com.ecommerce.catalog.dto.property;

import com.ecommerce.catalog.model.PropertyType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePropertyUnit implements Serializable {

    @JsonProperty(value = "property_type")
    private PropertyType propertyType;

    @JsonProperty(value = "translations")
    private List<CreatePropertyUnitTranslation> propertyUnitTranslationList;

}
