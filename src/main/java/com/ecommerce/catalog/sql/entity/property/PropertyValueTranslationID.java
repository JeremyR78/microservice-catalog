package com.ecommerce.catalog.sql.entity.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyValueTranslationID implements Serializable {

    private Integer propertyValue;
    private Integer language;

}
