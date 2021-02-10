package com.ecommerce.catalog.sql.entity.product;

import com.ecommerce.catalog.sql.entity.property.Property;
import com.ecommerce.catalog.sql.entity.property.PropertyValue;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;


@Data
@Entity
@Table(name = "product_property")
@IdClass( ProductPropertyID.class )
public class ProductProperty implements Serializable {

    @Id
    @JoinColumn(name = "pfk_id_product", nullable = false)
    @ManyToOne(optional = false)
    private Product product;

    @Id
    @JoinColumn(name = "pfk_id_property" , nullable = false )
    @ManyToOne(optional = false)
    private Property property;

    @JoinTable(
            name = "product_property_value_link",
            joinColumns = { @JoinColumn( name = "pfk_id_product", referencedColumnName="pfk_id_product"),
                            @JoinColumn( name = "pfk_id_property", referencedColumnName="pfk_id_property") } ,
            inverseJoinColumns = { @JoinColumn( name = "pfk_id_property_value", referencedColumnName="pk_id_property_value") }
    )
    @ManyToMany
    private List<PropertyValue> propertyValues;



}
