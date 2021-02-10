package com.ecommerce.catalog.dto.property;

import com.ecommerce.catalog.model.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePropertyValue implements Serializable {

    private PropertyType type;
    private Boolean valueBoolean;
    private Double valueNumber;
    private List<CreatePropertyValueTranslation> valueTranslations;

}
