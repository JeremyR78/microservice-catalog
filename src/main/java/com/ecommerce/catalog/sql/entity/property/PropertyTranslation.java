package com.ecommerce.catalog.sql.entity.property;

import com.ecommerce.catalog.sql.entity.common.Language;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name="property_translation")
@IdClass( PropertyTranslationID.class )
public class PropertyTranslation implements Serializable {

    @Id
    @JoinColumn(name = "pfk_id_property")
    @ManyToOne(optional = false)
    private Property property;

    @Id
    @JoinColumn(name = "pfk_id_language")
    @ManyToOne(optional = false)
    private Language language;

    private String label;

}
