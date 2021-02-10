package com.ecommerce.catalog.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LanguageTranslationDTO {

    private Integer id;
    @JsonProperty( value= "language_reference_id" )
    private Integer languageReferenceID;
    private String label;


}
