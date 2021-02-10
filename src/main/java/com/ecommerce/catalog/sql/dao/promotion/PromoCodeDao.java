package com.ecommerce.catalog.sql.dao.promotion;

import com.ecommerce.catalog.sql.entity.promotion.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PromoCodeDao extends JpaRepository<PromoCode, Integer>, JpaSpecificationExecutor<PromoCode> {
}
