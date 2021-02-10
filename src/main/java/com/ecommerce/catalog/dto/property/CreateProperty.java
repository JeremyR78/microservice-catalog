package com.ecommerce.catalog.dto.property;

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
public class CreateProperty implements Serializable {

    private Integer propertyUnitId;
    private boolean enable;
    private List<CreatePropertyTranslation> translations;
    private List<CreatePropertyValue> propertyValues;

}
