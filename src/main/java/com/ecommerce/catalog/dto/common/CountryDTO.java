package com.ecommerce.catalog.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryDTO implements Serializable {

    private Integer id;
    private Boolean enable;
    private List<CountryTranslationDTO> countryTranslationList;

}
