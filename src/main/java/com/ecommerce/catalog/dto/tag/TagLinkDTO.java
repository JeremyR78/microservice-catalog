package com.ecommerce.catalog.dto.tag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagLinkDTO implements Serializable  {

    private Integer tagParentId;
    private Integer tagChildId;

    private Integer linkStrength;

}
