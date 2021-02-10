package com.ecommerce.catalog.sql.entity.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPropertyID implements Serializable  {

    private Integer product;
    private Integer property;

}
