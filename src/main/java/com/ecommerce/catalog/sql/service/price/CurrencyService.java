package com.ecommerce.catalog.sql.service.price;

import com.ecommerce.catalog.dto.ConvertDTO;
import com.ecommerce.catalog.dto.common.StoreDTO;
import com.ecommerce.catalog.dto.price.CurrencyDTO;
import com.ecommerce.catalog.sql.dao.common.StoreDao;
import com.ecommerce.catalog.sql.dao.price.CurrencyDao;
import com.ecommerce.catalog.sql.entity.common.Store;
import com.ecommerce.catalog.sql.entity.price.Currency;
import com.ecommerce.catalog.service.AbstractService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService extends AbstractService<Currency, Integer> {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    private final CurrencyDao currencyDao;
    private final ConvertDTO<Currency, CurrencyDTO> dtoService;

    // --------------------------------------
    // -            Methods                 -
    // --------------------------------------

    @Override
    protected JpaRepository<Currency, Integer> getDao() {
        return this.currencyDao;
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
    public CurrencyDTO getByIdToDTO( Integer id )
    {
        return this.currencyDao.getByIdToDTO( id );
    }

    /**
     *
     * @return
     */
    public List<CurrencyDTO> findAllToDTO( )
    {
        return this.currencyDao.getAllToDTO();
    }

    //
    //  -   CONVERT
    //

    public List<CurrencyDTO> toDTO( List<Currency> currencies ){
        return this.dtoService.toDTO( currencies, CurrencyDTO.class );
    }

    public CurrencyDTO toDTO( Currency currencies ) {
        return this.dtoService.toDTO( currencies, CurrencyDTO.class );
    }

    public List<Currency> toEntity( List<CurrencyDTO> CurrencyDTOList ) {
        return this.dtoService.toEntity( CurrencyDTOList, Currency.class );
    }

    public Currency toEntity( CurrencyDTO currencyDTO ) {
        return this.dtoService.toEntity( currencyDTO, Currency.class );
    }


}
