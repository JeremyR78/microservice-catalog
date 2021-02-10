package com.ecommerce.catalog.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryTranslationDTO implements Serializable {

    private Integer countryId;
    private Integer languageId;
    private String label;

}
