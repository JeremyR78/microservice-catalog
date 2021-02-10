package com.ecommerce.catalog.dto.tag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTag implements Serializable  {

    private String label;

    private List<Integer> parentsTags;
    private List<Integer> childrenTags;

}
