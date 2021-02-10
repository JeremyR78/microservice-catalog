package com.ecommerce.catalog.sql.entity.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyTranslationID implements Serializable {

    private Integer property;
    private Integer language;



}
