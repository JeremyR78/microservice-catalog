package com.ecommerce.catalog.sql.entity.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryTranslationID implements Serializable {

    private Integer category;
    private Integer language;
}
