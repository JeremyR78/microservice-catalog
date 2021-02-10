package com.ecommerce.catalog.sql.dao.property;

import com.ecommerce.catalog.sql.entity.property.PropertyUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PropertyUnitDao extends JpaRepository<PropertyUnit, Integer>, JpaSpecificationExecutor {
}
