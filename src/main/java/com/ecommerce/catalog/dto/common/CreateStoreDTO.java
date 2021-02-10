package com.ecommerce.catalog.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateStoreDTO {

    private String code;
    private String label;
    private String webSite;


}
