package com.ecommerce.catalog.sql.dao.product;

import com.ecommerce.catalog.sql.entity.product.Dimension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DimensionDao extends JpaRepository<Dimension, Integer>, JpaSpecificationExecutor  {
}
