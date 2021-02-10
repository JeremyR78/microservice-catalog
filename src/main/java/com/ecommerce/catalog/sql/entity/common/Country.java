package com.ecommerce.catalog.sql.entity.common;

import com.ecommerce.catalog.sql.entity.tax.Tax;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "country")
public class Country implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_id_country", nullable = false)
    private Integer id;

    @OneToMany( mappedBy = "country", cascade = CascadeType.ALL )
    private List<CountryTranslation> countryTranslations;

    private Boolean enable;

    @OneToMany( mappedBy = "country", cascade = CascadeType.ALL )
    private List<Tax> tvaList;

}
