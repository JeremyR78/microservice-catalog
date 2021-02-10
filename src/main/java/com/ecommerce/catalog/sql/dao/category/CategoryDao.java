package com.ecommerce.catalog.sql.dao.category;

import com.ecommerce.catalog.sql.entity.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;


public interface CategoryDao extends JpaRepository<Category, Integer>,
        JpaSpecificationExecutor<Category>,
        QuerydslPredicateExecutor<Category>
{ }
