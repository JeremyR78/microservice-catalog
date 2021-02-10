package com.ecommerce.catalog.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPropertyStringLanguageDTO implements Serializable  {

    private Integer languageId;
    private String value;

}
