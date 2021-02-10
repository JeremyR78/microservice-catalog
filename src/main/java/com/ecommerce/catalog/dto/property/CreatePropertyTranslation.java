package com.ecommerce.catalog.dto.property;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePropertyTranslation implements Serializable {

    @JsonProperty(value = "language_id")
    private Integer languageId;

    private String label;

}
