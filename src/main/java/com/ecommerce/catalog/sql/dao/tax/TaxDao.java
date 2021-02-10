package com.ecommerce.catalog.sql.dao.tax;

import com.ecommerce.catalog.sql.entity.tax.Tax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TaxDao extends JpaRepository<Tax, Integer>, JpaSpecificationExecutor<Tax> {
}
