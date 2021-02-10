package com.ecommerce.catalog.sql.entity.property;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name="property")
public class Property implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_id_property", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "fk_id_property_unit")
    private PropertyUnit propertyUnit;

    private boolean enable;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL )
    private List<PropertyTranslation> translations;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL )
    private List<PropertyValue> propertyValues;

    @Override
    public String toString() {
        return "Property{" +
                "id=" + id +
                '}';
    }
}
