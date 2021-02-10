package com.ecommerce.catalog.sql.dao.customer;

import com.ecommerce.catalog.dto.customer.CustomerGroupDTO;
import com.ecommerce.catalog.dto.price.CurrencyDTO;
import com.ecommerce.catalog.sql.entity.customer.CustomerGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerGroupDao extends JpaRepository<CustomerGroup, Integer>, JpaSpecificationExecutor {

    @Query(
            value="SELECT new com.ecommerce.catalog.dto.customer.CustomerGroupDTO( c.id, c.type, c.label ) " +
                    "FROM CustomerGroup c " +
                    "WHERE c.id = ?1 "
    )
    CustomerGroupDTO getByIdToDTO(Integer storeId );

    @Query(
            value="SELECT new com.ecommerce.catalog.dto.customer.CustomerGroupDTO( c.id, c.type, c.label ) " +
                    "FROM CustomerGroup c "
    )
    List<CustomerGroupDTO> getAllToDTO();

}
