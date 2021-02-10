package com.ecommerce.catalog.sql.entity.property;

import com.ecommerce.catalog.sql.entity.common.Language;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "property_value_translation" )
@IdClass(PropertyValueTranslationID.class)
public class PropertyValueTranslation implements Serializable {

    @Id
    @JoinColumn(name = "pfk_id_property_value")
    @ManyToOne(optional = false)
    private PropertyValueString propertyValue;

    @Id
    @JoinColumn(name = "pfk_id_language")
    @ManyToOne(optional = false)
    private Language language;

    private String label;

}
