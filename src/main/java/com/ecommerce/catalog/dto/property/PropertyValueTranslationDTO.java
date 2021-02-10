package com.ecommerce.catalog.dto.property;

import com.ecommerce.catalog.sql.entity.common.Language;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyValueTranslationDTO implements Serializable {

    private Integer languageId;
    private String label;

}
