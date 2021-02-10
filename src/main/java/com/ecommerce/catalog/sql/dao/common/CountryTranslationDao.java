package com.ecommerce.catalog.sql.dao.common;

import com.ecommerce.catalog.sql.entity.common.CountryTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CountryTranslationDao extends JpaRepository<CountryTranslation, Integer>, JpaSpecificationExecutor {
}
