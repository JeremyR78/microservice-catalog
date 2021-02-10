package com.ecommerce.catalog.dto.tax;

import com.ecommerce.catalog.model.TaxType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrUpdateTax {

    private Integer countryId;
    private TaxType taxType;
    private BigDecimal percent;

}
