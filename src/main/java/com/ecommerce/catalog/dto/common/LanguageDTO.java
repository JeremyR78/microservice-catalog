package com.ecommerce.catalog.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LanguageDTO {

    private Integer id;
    @JsonProperty( value= "iso_code" )
    private String isoCode;
    private Boolean active;

    @JsonProperty( value= "translations" )
    private List<LanguageTranslationDTO> translations;


}
