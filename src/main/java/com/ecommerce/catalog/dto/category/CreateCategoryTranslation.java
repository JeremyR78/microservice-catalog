package com.ecommerce.catalog.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCategoryTranslation implements Serializable  {

    private Integer languageId;
    private String label;

}
