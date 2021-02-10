package com.ecommerce.catalog.sql.dao.common;

import com.ecommerce.catalog.sql.entity.common.Store;
import com.ecommerce.catalog.dto.common.StoreDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreDao extends JpaRepository<Store, Integer>, JpaSpecificationExecutor {


    @Query(
            value="SELECT new com.ecommerce.catalog.dto.common.StoreDTO( s.id, s.code, s.label, s.webSite ) " +
                    "FROM Store s " +
                    "WHERE s.id = ?1 "
    )
    StoreDTO getByIdToDTO( Integer storeId );

    @Query(
            value="SELECT new com.ecommerce.catalog.dto.common.StoreDTO( s.id, s.code, s.label, s.webSite ) " +
                    "FROM Store s "
    )
    List<StoreDTO> getAllToDTO();

}
