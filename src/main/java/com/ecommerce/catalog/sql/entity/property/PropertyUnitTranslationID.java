package com.ecommerce.catalog.sql.entity.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyUnitTranslationID implements Serializable {

    private Integer propertyUnit;
    private Integer language;
}
