package com.ecommerce.catalog.sql.entity.common;


import com.ecommerce.catalog.sql.entity.category.CategoryTranslationID;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "country_translation")
@IdClass( CountryTranslationID.class )
public class CountryTranslation implements Serializable  {

    @Id
    @JoinColumn(name="pfk_id_country")
    @ManyToOne(optional = false)
    private Country country;

    @Id
    @JoinColumn(name="pfk_id_language")
    @ManyToOne(optional = false)
    private Language language;

    private String label;

}
