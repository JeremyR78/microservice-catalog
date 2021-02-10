package com.ecommerce.catalog.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductProperty implements Serializable {

    private Integer propertyId;
    private List<Integer> propertyValueIdList;

}
