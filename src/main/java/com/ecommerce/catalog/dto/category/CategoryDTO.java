package com.ecommerce.catalog.dto.category;

import com.ecommerce.catalog.dto.tag.TagDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO implements Serializable {

    private Integer id;
    private Integer storeId;
    private Boolean visible;
    private Integer parentId;
    private Integer rank;
    private List<Integer> childrenId;
    private String pathImage;

    private List<CategoryTranslationDTO> categoryTranslationDTOList;

    private List<TagDTO> tags;

}
