package com.ecommerce.catalog.sql.entity.property;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name="property_value_string")
@DiscriminatorValue( "STRING")
public class PropertyValueString extends PropertyValue {

    @OneToMany(mappedBy = "propertyValue" , cascade = CascadeType.ALL )
    private List<PropertyValueTranslation> translationList;

}
