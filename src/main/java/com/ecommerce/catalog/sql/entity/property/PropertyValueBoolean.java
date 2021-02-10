package com.ecommerce.catalog.sql.entity.property;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name="property_value_boolean")
@DiscriminatorValue( "BOOLEAN")
public class PropertyValueBoolean extends PropertyValue {

    @Column( name = "value_bool" )
    public Boolean value;

}
