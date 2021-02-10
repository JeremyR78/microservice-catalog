package com.ecommerce.catalog.sql.entity.product;

import com.ecommerce.catalog.sql.entity.property.PropertyValue;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 *  Just a one property value different between product
 *
 *
 */
@Data
@Entity
@Table(name = "product_link")
public class ProductLink implements Serializable {

    @Id
    @Column(name = "pk_id_product_link")
    private Integer id;

    /**
     * Product of reference
     */
    @JoinColumn(name = "fk_id_product", nullable = false)
    @ManyToOne(optional = false)
    private Product product;

    /**
     * Property
     */
    @JoinTable(
            name = "product_link_property_value_link",
            joinColumns = { @JoinColumn(name = "pfk_id_product_link") },
            inverseJoinColumns = { @JoinColumn(name = "pfk_id_property_value") }
    )
    @ManyToMany
    private List<PropertyValue> propertyValues;


    /**
     * Properties values of others products
     */
    @JoinTable(
            name = "product_link_link",
            joinColumns = { @JoinColumn(name = "pfk_id_product_link_first") },
            inverseJoinColumns = { @JoinColumn(name = "pfk_id_product_link_second") }
    )
    @ManyToMany
    private List<ProductLink> productLinks;




}
