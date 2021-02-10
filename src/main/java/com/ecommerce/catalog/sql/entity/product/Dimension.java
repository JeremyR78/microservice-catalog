package com.ecommerce.catalog.sql.entity.product;

import com.ecommerce.catalog.model.DimensionType;
import com.ecommerce.catalog.sql.entity.property.Property;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name="dimension")
public class Dimension implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_id_dimension", nullable = false)
    private Integer id;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "dimension_type")
    private DimensionType dimensionType;

    @JoinTable(
            name = "dimension_property_link",
            joinColumns = { @JoinColumn(name = "pfk_id_dimension") },
            inverseJoinColumns = { @JoinColumn(name = "pfk_id_property") }
    )
    @ManyToMany
    List<Property> propertiesDimension;

}
