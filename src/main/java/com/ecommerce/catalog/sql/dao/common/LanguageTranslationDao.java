package com.ecommerce.catalog.sql.dao.common;

import com.ecommerce.catalog.sql.entity.common.LanguageTranslation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface LanguageTranslationDao extends JpaRepository<LanguageTranslation, Integer>, JpaSpecificationExecutor {

}
