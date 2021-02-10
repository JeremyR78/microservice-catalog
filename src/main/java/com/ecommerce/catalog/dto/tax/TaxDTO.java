package com.ecommerce.catalog.dto.tax;

import com.ecommerce.catalog.model.TaxType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxDTO implements Serializable {

    private Integer id;
    private Integer countryId;
    private TaxType taxType;
    private BigDecimal percent;

}
