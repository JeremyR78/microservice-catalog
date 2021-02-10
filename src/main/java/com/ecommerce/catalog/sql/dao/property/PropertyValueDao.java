package com.ecommerce.catalog.sql.dao.property;

import com.ecommerce.catalog.sql.entity.property.PropertyValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PropertyValueDao extends JpaRepository<PropertyValue, Integer>,
        JpaSpecificationExecutor<PropertyValue>,
        QuerydslPredicateExecutor<PropertyValue>
{

}
