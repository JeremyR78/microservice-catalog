package com.ecommerce.catalog.sql.dao.common;

import com.ecommerce.catalog.sql.entity.common.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LanguageDao extends JpaRepository<Language, Integer>, JpaSpecificationExecutor {

//
//    @Query(
//            value="SELECT new com.ecommerce.product.dto.common.LanguageDTO( s.id, s.code, s.label, s.webSite ) " +
//                    "FROM Store s " +
//                    "WHERE s.id = ?1 "
//    )
//    LanguageDTO getByIdToDTO(Integer storeId );
//
//    @Query(
//            value="SELECT new com.ecommerce.product.dto.common.LanguageDTO( s.id, s.code, s.label, s.webSite ) " +
//                    "FROM Store s "
//    )
//    List<LanguageDTO> getAllToDTO();

}
