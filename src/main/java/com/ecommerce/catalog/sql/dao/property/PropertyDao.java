package com.ecommerce.catalog.sql.dao.property;


import com.ecommerce.catalog.sql.entity.property.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PropertyDao extends JpaRepository<Property, Integer>, JpaSpecificationExecutor<Property> {
}
