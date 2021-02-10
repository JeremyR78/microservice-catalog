package com.ecommerce.catalog.nosql.data;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@RequiredArgsConstructor
public class CategoryEs implements Serializable {

    private Integer id;
    private String label;
    private Boolean visible;
    private Integer parentId;
    private Integer rank;
    private List<Integer> childrenId;

}
