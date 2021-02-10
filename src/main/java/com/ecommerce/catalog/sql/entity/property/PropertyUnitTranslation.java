package com.ecommerce.catalog.sql.entity.property;

import com.ecommerce.catalog.sql.entity.common.Language;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@IdClass(PropertyUnitTranslationID.class)
@Table(name="property_unit_translation")
public class PropertyUnitTranslation implements Serializable {

    @Id
    @JoinColumn(name = "pfk_id_property_unit", referencedColumnName = "pk_id_property_unit")
    @ManyToOne(optional = false)
    private PropertyUnit propertyUnit;

    @Id
    @JoinColumn(name = "pfk_id_language", referencedColumnName = "pk_id_language")
    @ManyToOne(optional = false)
    private Language language;

    private String label;

    @Column(name = "label_lite")
    private String labelLite;

}
