package com.ecommerce.catalog.sql.entity.property;

import com.ecommerce.catalog.model.PropertyType;
import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name="property_unit")
public class PropertyUnit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pk_id_property_unit", nullable = false)
    private Integer id;

    @Enumerated(value = EnumType.STRING)
    @Column(name="property_type")
    private PropertyType propertyType;

    @OneToMany(mappedBy = "propertyUnit", cascade = CascadeType.ALL )
    private List<PropertyUnitTranslation> propertyUnitTranslationList;


    @Override
    public String toString() {
        return "PropertyUnit{" +
                "id=" + id +
                ", propertyType=" + propertyType +
                '}';
    }
}
