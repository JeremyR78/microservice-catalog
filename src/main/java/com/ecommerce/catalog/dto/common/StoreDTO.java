package com.ecommerce.catalog.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreDTO implements Serializable {

    private Integer id;
    private String code;
    private String label;
    @JsonProperty( value= "web_site" )
    private String webSite;

}
