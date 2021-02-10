package com.ecommerce.catalog.sql.entity.property;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name="property_value")
@Inheritance( strategy = InheritanceType.SINGLE_TABLE )
@DiscriminatorColumn( name = "value_type", discriminatorType = DiscriminatorType.STRING )
public abstract class PropertyValue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( name = "pk_id_property_value" )
    private Integer id;

    @JoinColumn(name = "fk_id_property" )
    @ManyToOne(optional = false)
    private Property property;

}
