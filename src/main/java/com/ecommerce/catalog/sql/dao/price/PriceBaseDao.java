package com.ecommerce.catalog.sql.dao.price;

import com.ecommerce.catalog.sql.entity.price.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface PriceBaseDao<T extends Price> extends JpaRepository<T, Integer>,
        JpaSpecificationExecutor<T>,
        QuerydslPredicateExecutor<T> {


}
