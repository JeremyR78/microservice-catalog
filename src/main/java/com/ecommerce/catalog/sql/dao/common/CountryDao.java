package com.ecommerce.catalog.sql.dao.common;

import com.ecommerce.catalog.sql.entity.common.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CountryDao extends JpaRepository<Country, Integer>, JpaSpecificationExecutor {
}
