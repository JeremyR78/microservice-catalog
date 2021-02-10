package com.ecommerce.catalog.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCategory implements Serializable {


    private Integer storeId;
    private Boolean visible;
    private Integer parentId;
    private Integer rank;
    private List<Integer> childrenId;

    private List<CreateCategoryTranslation> translations;
    private List<Integer> tagsId;

}
