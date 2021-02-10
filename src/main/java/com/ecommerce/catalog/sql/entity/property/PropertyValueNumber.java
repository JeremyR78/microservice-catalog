package com.ecommerce.catalog.sql.entity.property;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="property_value_number")
@DiscriminatorValue( "NUMBER")
public class PropertyValueNumber extends PropertyValue {

    @Column( name = "value_number" )
    private Double value;

}
