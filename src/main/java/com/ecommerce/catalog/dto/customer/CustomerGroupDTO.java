package com.ecommerce.catalog.dto.customer;

import com.ecommerce.catalog.model.CustomerGroupType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerGroupDTO implements Serializable {

    private Integer id;
    private CustomerGroupType type;
    private String label;


}
