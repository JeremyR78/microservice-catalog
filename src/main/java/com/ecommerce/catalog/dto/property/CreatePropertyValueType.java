package com.ecommerce.catalog.dto.property;

import com.ecommerce.catalog.model.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePropertyValueType implements Serializable {

    private Integer id;
    private PropertyType type;

}
