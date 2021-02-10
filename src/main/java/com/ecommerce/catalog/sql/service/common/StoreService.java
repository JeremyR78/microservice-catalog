package com.ecommerce.catalog.sql.service.common;

import com.ecommerce.catalog.sql.entity.common.Store;
import com.ecommerce.catalog.service.AbstractService;
import com.ecommerce.catalog.sql.dao.common.StoreDao;
import com.ecommerce.catalog.dto.ConvertDTO;
import com.ecommerce.catalog.dto.common.StoreDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreService extends AbstractService<Store, Integer> {

    // --------------------------------------
    // -        Services                    -
    // --------------------------------------

    private final StoreDao storeDao;
    private final ConvertDTO<Store, StoreDTO> dtoService;

    // --------------------------------------
    // -        Methods                     -
    // --------------------------------------

    @Override
    protected StoreDao getDao() {
        return this.storeDao;
    }

    //
    //  -   READ ONLY
    //

    /**
     *
     *
     * @param id
     * @return
     */
    public StoreDTO getByIdToDTO( Integer id )
    {
        return this.storeDao.getByIdToDTO( id );
    }

    /**
     *
     * @return
     */
    public List<StoreDTO> findAllToDTO( )
    {
        return this.storeDao.getAllToDTO();
    }

    //
    //  -   CONVERT
    //

    public List<StoreDTO> toDTO( List<Store> stores ){
        return this.dtoService.toDTO( stores, StoreDTO.class );
    }

    public StoreDTO toDTO( Store store ) {
        return this.dtoService.toDTO( store, StoreDTO.class );
    }

    public List<Store> toEntity( List<StoreDTO> storeDTOList ) {
        return this.dtoService.toEntity( storeDTOList, Store.class );
    }

    public Store toEntity( StoreDTO storeDTO ) {
        return this.dtoService.toEntity( storeDTO, Store.class );
    }

}
