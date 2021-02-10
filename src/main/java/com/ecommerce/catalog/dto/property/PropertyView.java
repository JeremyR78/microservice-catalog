package com.ecommerce.catalog.dto.property;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyView implements Serializable  {

    private Integer id;
    private boolean enable;
    private String label;
    private String unitLabel;
    private String unitLabelLite;

}
