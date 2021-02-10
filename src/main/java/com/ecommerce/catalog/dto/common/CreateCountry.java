package com.ecommerce.catalog.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCountry implements Serializable {

    private Boolean enable;
    private List<CreateCountryTranslation> countryTranslations;

}
