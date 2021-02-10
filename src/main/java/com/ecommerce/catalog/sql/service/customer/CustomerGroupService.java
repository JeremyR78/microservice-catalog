package com.ecommerce.catalog.sql.service.customer;

import com.ecommerce.catalog.dto.ConvertDTO;
import com.ecommerce.catalog.dto.common.StoreDTO;
import com.ecommerce.catalog.dto.customer.CustomerGroupDTO;
import com.ecommerce.catalog.dto.price.CurrencyDTO;
import com.ecommerce.catalog.service.AbstractService;
import com.ecommerce.catalog.sql.dao.customer.CustomerGroupDao;
import com.ecommerce.catalog.sql.entity.customer.CustomerGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerGroupService extends AbstractService<CustomerGroup, Integer>  {

    // --------------------------------------
    // -        Services                    -
    // --------------------------------------

    private final CustomerGroupDao customerGroupDao;
    private final ConvertDTO<CustomerGroup, CustomerGroupDTO> dtoService;

    // --------------------------------------
    // -        Methods                     -
    // --------------------------------------

    @Override
    protected CustomerGroupDao getDao() {
        return this.customerGroupDao;
    }

    public List<CustomerGroupDTO> findAllToDTO( ) {
        return this.customerGroupDao.getAllToDTO();
    }

    public CustomerGroupDTO getByIdToDTO( Integer id )
    {
        return this.customerGroupDao.getByIdToDTO( id );
    }

    //
    //  -   CONVERT
    //

    public List<CustomerGroupDTO> toDTO(List<CustomerGroup> customerGroups ){
        return this.dtoService.toDTO( customerGroups, CustomerGroupDTO.class );
    }

    public CustomerGroupDTO toDTO( CustomerGroup customerGroup ) {
        return this.dtoService.toDTO( customerGroup, CustomerGroupDTO.class );
    }

    public List<CustomerGroup> toEntity( List<CustomerGroupDTO> customerGroupDTOList ) {
        return this.dtoService.toEntity( customerGroupDTOList, CustomerGroup.class );
    }

    public CustomerGroup toEntity( CustomerGroupDTO customerGroupDTO ) {
        return this.dtoService.toEntity( customerGroupDTO, CustomerGroup.class );
    }

}
