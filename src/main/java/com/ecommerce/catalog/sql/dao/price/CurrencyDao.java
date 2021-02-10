package com.ecommerce.catalog.sql.dao.price;

import com.ecommerce.catalog.dto.price.CurrencyDTO;
import com.ecommerce.catalog.sql.entity.price.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CurrencyDao extends JpaRepository<Currency, Integer>, JpaSpecificationExecutor {


    @Query(
            value="SELECT new com.ecommerce.catalog.dto.price.CurrencyDTO( c.id, c.symbol, c.label ) " +
                    "FROM Currency c " +
                    "WHERE c.id = ?1 "
    )
    CurrencyDTO getByIdToDTO(Integer storeId );

    @Query(
            value="SELECT new com.ecommerce.catalog.dto.price.CurrencyDTO( c.id, c.symbol, c.label ) " +
                    "FROM Currency c "
    )
    List<CurrencyDTO> getAllToDTO();

}
