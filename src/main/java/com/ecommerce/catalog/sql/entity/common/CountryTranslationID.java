package com.ecommerce.catalog.sql.entity.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryTranslationID implements Serializable {

    private Integer country;
    private Integer language;

}
