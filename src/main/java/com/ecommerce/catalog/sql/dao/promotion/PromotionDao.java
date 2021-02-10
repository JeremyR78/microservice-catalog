package com.ecommerce.catalog.sql.dao.promotion;

import com.ecommerce.catalog.sql.entity.promotion.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface PromotionDao extends JpaRepository<Promotion, Integer>, JpaSpecificationExecutor<Promotion> {

    /**
     *  Récupére la liste des promotions ACTIVEE pour un produit
     *
     * @param currentDate
     * @return
     */
    @Query(
            value="SELECT p " +
                    "FROM Promotion p " +
                    "WHERE p.enable = true "+
                    "AND p.startAt < :currentDate " +
                    "AND p.finishAt > :currentDate "
    )
    List<Promotion> getPromotionsByProduct( @Param("currentDate") Date currentDate );
}
