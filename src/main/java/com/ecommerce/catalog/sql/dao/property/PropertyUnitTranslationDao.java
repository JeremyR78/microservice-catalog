package com.ecommerce.catalog.sql.dao.property;

import com.ecommerce.catalog.sql.entity.property.PropertyUnitTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PropertyUnitTranslationDao extends JpaRepository<PropertyUnitTranslation, Integer>,
        JpaSpecificationExecutor<PropertyUnitTranslation> {
}
