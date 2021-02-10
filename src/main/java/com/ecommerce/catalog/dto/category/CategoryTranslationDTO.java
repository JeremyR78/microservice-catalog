package com.ecommerce.catalog.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryTranslationDTO implements Serializable  {

    private Integer id;
    private Integer languageId;
    private String label;
}
