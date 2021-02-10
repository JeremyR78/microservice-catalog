package com.ecommerce.catalog.sql.entity.tag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagLinkID implements Serializable {

    private Integer tagParent;
    private Integer tagChild;

}
