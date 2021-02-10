package com.ecommerce.catalog.dto.property;

import com.ecommerce.catalog.model.PropertyType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyValueDTO implements Serializable {

    private Integer id;
    private PropertyType type;
    private Boolean valueBoolean;
    private Double valueNumber;
    private List<PropertyValueTranslationDTO> valueTranslations;

}
