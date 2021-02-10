package com.ecommerce.catalog.sql.dao.category;

import com.ecommerce.catalog.sql.entity.category.CategoryTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoryTranslationDao  extends JpaRepository<CategoryTranslation, Integer>, JpaSpecificationExecutor  {
}
