package com.ecommerce.catalog.sql.entity.tax;

import com.ecommerce.catalog.model.TaxType;
import com.ecommerce.catalog.sql.entity.common.Country;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "tax")
public class Tax implements Serializable  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_id_tax", nullable = false)
    private Integer id;

    @JoinColumn(name = "fk_id_country", referencedColumnName = "pk_id_country")
    @ManyToOne(optional = false)
    private Country country;

    private TaxType taxType;

    private BigDecimal percent;

}
