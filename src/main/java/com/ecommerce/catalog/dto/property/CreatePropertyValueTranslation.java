package com.ecommerce.catalog.dto.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePropertyValueTranslation implements Serializable {

    private Integer languageId;
    private String value;

}
