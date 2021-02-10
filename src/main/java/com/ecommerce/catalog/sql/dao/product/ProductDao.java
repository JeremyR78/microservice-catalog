package com.ecommerce.catalog.sql.dao.product;

import com.ecommerce.catalog.sql.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface ProductDao extends JpaRepository<Product, Integer>,
        JpaSpecificationExecutor<Product>, QuerydslPredicateExecutor<Product> {



}
