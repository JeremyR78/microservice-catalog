package com.ecommerce.catalog.sql.dao.property;

import com.ecommerce.catalog.sql.entity.property.PropertyTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PropertyTranslationDao extends JpaRepository<PropertyTranslation, Integer>,
        JpaSpecificationExecutor<PropertyTranslation>  {
}
