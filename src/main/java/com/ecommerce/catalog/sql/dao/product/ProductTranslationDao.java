package com.ecommerce.catalog.sql.dao.product;

import com.ecommerce.catalog.sql.entity.product.ProductTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductTranslationDao extends JpaRepository<ProductTranslation, Integer>, JpaSpecificationExecutor {
}
